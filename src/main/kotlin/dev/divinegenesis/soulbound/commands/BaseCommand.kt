package dev.divinegenesis.soulbound.commands

import dev.divinegenesis.soulbound.Utils
import dev.divinegenesis.soulbound.logger
import dev.divinegenesis.soulbound.storage.DataStack
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
        PICKUP
    }

    private val enumParameter = Parameter.enumValue(Choices::class.java).key("enum").build()

    private val bindCommand = builder()
        .shortDescription(Component.text("Binds the item to the player holding it"))
        .executor(this::bindCommandResult)
        .permission("")
        .build()
    private val setStorage = builder()
        .shortDescription(Component.text("Adds the item to the database!"))
        .executor(this::storageCommandResult)
        .addParameter(Parameter.seq(enumParameter, CommonParameters.BOOLEAN))
        .permission("")
        .build()

    //Base Command for above commands, as commands are added, create additional children
    var helpCommand = builder()
        .shortDescription(Component.text("Shows help"))
        .executor(this::helpCommandResult)
        .permission("")
        .addChild(bindCommand, "bind")
        .addChild(setStorage, "set","add")
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
            sender.sendMessage(Component.text("${sql.loadData().size} entries"))
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
            val finalStack = Utils().sortData(itemStack, sender.uniqueId()).first
            sender.setItemInHand(HandTypes.MAIN_HAND, finalStack)
        }
        return CommandResult.success()
    }
}

fun Boolean.toInt() = if (this) 1 else 0

