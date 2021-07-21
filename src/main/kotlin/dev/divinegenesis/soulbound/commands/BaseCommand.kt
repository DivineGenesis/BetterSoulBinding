package dev.divinegenesis.soulbound.commands

import net.kyori.adventure.text.Component
import org.spongepowered.api.command.CommandExecutor
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.CommandContext
import org.spongepowered.api.command.exception.CommandException
import org.spongepowered.api.entity.living.player.server.ServerPlayer


class BaseCommand : CommandExecutor {
    @Throws(CommandException::class)
    override fun execute(context: CommandContext): CommandResult {

        val sender = context.cause().root() as ServerPlayer

        sender.sendMessage(Component.text("We did it!"))

        return CommandResult.success()
    }
}

