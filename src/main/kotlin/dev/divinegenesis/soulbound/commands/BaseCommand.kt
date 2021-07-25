package dev.divinegenesis.soulbound.commands

import dev.divinegenesis.soulbound.Soulbound
import dev.divinegenesis.soulbound.customdata.DataStack
import dev.divinegenesis.soulbound.customdata.DataUtilities
import dev.divinegenesis.soulbound.customdata.toInt
import dev.divinegenesis.soulbound.getID
import dev.divinegenesis.soulbound.logger
import dev.divinegenesis.soulbound.storage.SqliteDatabase
import net.kyori.adventure.text.Component
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command.*
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.exception.CommandException
import org.spongepowered.api.command.parameter.CommonParameters
import org.spongepowered.api.command.parameter.Parameter
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.registry.RegistryTypes


class BaseCommand {

    enum class Choices {
        INTERACT,
        CRAFT,
        PICKUP;

        override fun toString(): String {
            return super.toString().toLowerCase().capitalize() //todo: Test this
        }
    }

    private val enumParameter = Parameter.enumValue(Choices::class.java).key("enum").build()

    private val bindCommand = builder()
        .shortDescription(Component.text("Binds the item with blank data to be bound later"))
        .executor(this::bindCommandResult)
        .permission("")
        .build()

    private val removeBindCommand = builder()
        .shortDescription(Component.text("Removes binding from the item the player is holding"))
        .executor(this::removeBindCommandResult)
        .permission("")
        .build()

    private val setStorageCommand = builder()
        .shortDescription(Component.text("Adds the item to the database!"))
        .executor(this::storageCommandResult)
        .addParameter(Parameter.seq(enumParameter, CommonParameters.BOOLEAN))
        .permission("")
        .build()

    private val checkBindingCommand = builder()
        .shortDescription(Component.text("Checks if an item has bind data on it!"))
        .executor(this::checkBindingResult)
        .permission("")
        .build()

    //Base Command for above commands, as commands are added, create additional children
    var helpCommand = builder()
        .shortDescription(Component.text("Shows help"))
        .executor(this::helpCommandResult)
        .permission("")
        .addChild(bindCommand, "bind")
        .addChild(removeBindCommand, "unbind")
        .addChild(setStorageCommand, "set", "add")
        .addChild(checkBindingCommand, "check")
        .build()

    @Throws(CommandException::class)
    private fun helpCommandResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()

        if (sender is ServerPlayer) {
            sender.sendMessage(
                Component.text(
                    """
                Welcome to Soulbound
                /sb is the root command
                /sb bind will let you bind an item
            """.trimIndent()
                )
            )
        } else {
            return errorText
        }
        return CommandResult.success()
    }

    @Throws(CommandException::class)
    private fun checkBindingResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()
        if (sender is ServerPlayer) {
            val itemStack = sender.itemInHand(HandTypes.MAIN_HAND)
            if (itemStack.type().isAnyOf(ItemTypes.AIR)) {
                return CommandResult.error(Component.text("You need to have an item in your main hand!"))
            }
            val isData = DataUtilities().containsData(itemStack)
            val dataStack = Soulbound.database[itemStack.getID()]
            sender.sendMessage(
                Component.text(
                    """
                Is data applied? : $isData
                
                        -Configuration-
                ItemStack: ${dataStack?.itemID}
                Interact: ${dataStack?.interact}
                Pickup: ${dataStack?.pickup}
                Craft: ${dataStack?.craft}
            """.trimIndent()
                )
            )
        } else {
            return errorText
        }
        return CommandResult.success()
    }

    @Throws(CommandException::class)
    private fun storageCommandResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()
        val sql = SqliteDatabase()

        if (sender is ServerPlayer) {
            val itemType = sender.itemInHand(HandTypes.MAIN_HAND).type()
            if (itemType.isAnyOf(ItemTypes.AIR)) {
                return CommandResult.error(Component.text("You need to have an item in your main hand!"))
            }
            val itemID = Sponge.game().registries().registry(RegistryTypes.ITEM_TYPE).valueKey(itemType).formatted()
            val dataStack = sql.loadData()[itemID] ?: DataStack(itemID)
            val checkInteraction = context.one(CommonParameters.BOOLEAN).get()

            when (context.one(enumParameter).get()) {
                Choices.INTERACT -> {
                    dataStack.interact = checkInteraction.toInt()
                }
                Choices.CRAFT -> {
                    dataStack.craft = checkInteraction.toInt()
                }
                Choices.PICKUP -> {
                    dataStack.pickup = checkInteraction.toInt()
                }
            }
            sql.saveStack(dataStack)
            Soulbound.database = sql.loadData() //Refresh connection
            logger<BaseCommand>().info("Database entries: ${Soulbound.database.size}")
        } else {
            return errorText
        }
        return CommandResult.success()
    }


    @Throws(CommandException::class)
    private fun bindCommandResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()

        if (sender is ServerPlayer) {
            val itemStack = sender.itemInHand(HandTypes.MAIN_HAND)
            if (itemStack.type().isAnyOf(ItemTypes.AIR)) {
                return CommandResult.error(Component.text("You need to have an item in your main hand!"))
            }
            val finalStack = DataUtilities().sortData(itemStack, DataUtilities.blankUUID).first

            sender.setItemInHand(HandTypes.MAIN_HAND, finalStack)
        } else {
            return errorText
        }
        return CommandResult.success()
    }

    @Throws(CommandException::class)
    private fun removeBindCommandResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()

        if (sender is ServerPlayer) {
            val itemStack = sender.itemInHand(HandTypes.MAIN_HAND)
            if (itemStack.type().isAnyOf(ItemTypes.AIR)) {
                return CommandResult.error(Component.text("You need to have an item in your main hand!"))
            }
            DataUtilities().removeData(itemStack)
            sender.setItemInHand(HandTypes.MAIN_HAND, itemStack)
            logger<BaseCommand>().info(DataUtilities().containsData(itemStack))
        } else {
            return errorText
        }
        return CommandResult.success()
    }

    private val errorText = CommandResult.error(Component.text("This command must be ran by a player!"))
}



