package dev.divinegenesis.soulbound.commands

import dev.divinegenesis.soulbound.Soulbound
import dev.divinegenesis.soulbound.customdata.*
import dev.divinegenesis.soulbound.logger
import dev.divinegenesis.soulbound.storage.SqliteDatabase
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
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
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.service.pagination.PaginationList


class BaseCommand {

    enum class Choices {
        INTERACT,
        CRAFT,
        PICKUP;
    }

    private val enumParameter = Parameter.enumValue(Choices::class.java).key("enum").build()

    private val bindCommand = builder()
        .shortDescription(!"Binds the item with blank data to be bound later")
        .executor(this::bindCommandResult)
        .permission("soulbound.command.bind.add")
        .build()

    private val removeBindCommand = builder()
        .shortDescription(!"Removes binding from the item the player is holding")
        .executor(this::removeBindCommandResult)
        .permission("soulbound.command.bind.remove")
        .build()

    private val setStorageCommand = builder()
        .shortDescription(!"Adds the item to the database!")
        .executor(this::storageCommandResult)
        .addParameter(Parameter.seq(enumParameter, CommonParameters.BOOLEAN))
        .permission("soulbound.command.set")
        .build()

    private val checkBindingCommand = builder()
        .shortDescription(!"Checks if an item has bind data on it!")
        .executor(this::checkBindingResult)
        .permission("soulbound.command.check")
        .build()

    //Base Command for above commands, as commands are added, create additional children
    var helpCommand = builder()
        .shortDescription(!"Shows help")
        .executor(this::helpCommandResult)
        .permission("soulbound.command.help")
        .addChild(bindCommand, "bind")
        .addChild(removeBindCommand, "unbind")
        .addChild(setStorageCommand, "set", "add")
        .addChild(checkBindingCommand, "check")
        .build()

    @Throws(CommandException::class)
    private fun helpCommandResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()

        if (sender is ServerPlayer) {
            paginationBuilder(
                !"""
                Welcome to Soulbound
                /sb is the root command
                /sb bind will let you bind an item
                """.trimIndent()
            ).sendTo(sender)
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
                return CommandResult.error(!"You need to have an item in your main hand!")
            }
            val itemID = itemStack.getID()
            val dataStack = Soulbound.database[itemID] ?: DataStack(itemID)
            checkStackText(itemStack, dataStack).sendTo(sender)
        } else {
            return errorText
        }
        return CommandResult.success()
    }

    @Throws(CommandException::class)
    private fun storageCommandResult(context: CommandContext): CommandResult {

        val sender = context.cause().root()
        val sql = SqliteDatabase()
        val databaseCache = Soulbound.database

        if (sender is ServerPlayer) {
            val itemStack = sender.itemInHand(HandTypes.MAIN_HAND)
            if (itemStack.type().isAnyOf(ItemTypes.AIR)) {
                return CommandResult.error(!"You need to have an item in your main hand!")
            }
            val itemID = itemStack.getID()
            val dataStack = databaseCache[itemID] ?: DataStack(itemID)
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
            checkStackText(itemStack, dataStack).sendTo(sender)
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
                return CommandResult.error(!"You need to have an item in your main hand!")
            }
            val finalStack = DataUtilities().sortData(itemStack, DataUtilities.blankUUID).stack()

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
                return CommandResult.error(!"You need to have an item in your main hand!")
            }
            DataUtilities().removeData(itemStack)
            sender.setItemInHand(HandTypes.MAIN_HAND, itemStack)
            logger<BaseCommand>().info(DataUtilities().containsData(itemStack))
        } else {
            return errorText
        }
        return CommandResult.success()
    }

    private val errorText = CommandResult.error(!"This command must be ran by a player!")

    private fun checkStackText(itemStack: ItemStack, dataStack: DataStack?): PaginationList {
        val hasData = DataUtilities().containsData(itemStack)

        return paginationBuilder(
            !
            """
            Is item soulbound? : $hasData
                        
                        -Configuration-
            ItemStack   :   ${dataStack?.itemID}
            Interact    :   ${dataStack?.interact}
            Pickup       :   ${dataStack?.pickup}
            Craft        :   ${dataStack?.craft}
            """.trimIndent()
        )
    }

    private fun paginationBuilder(component: Component): PaginationList {
        val paginationService = Sponge.serviceProvider().paginationService()

        return paginationService.builder()
            .title(!"Soulbound")
            .padding((!"=").decorate(TextDecoration.STRIKETHROUGH).color(TextColor.color(200, 200, 50)))
            .contents(component)
            .build()

    }
}



