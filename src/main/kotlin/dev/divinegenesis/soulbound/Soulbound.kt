package dev.divinegenesis.soulbound

import org.apache.logging.log4j.Logger
import org.spongepowered.plugin.jvm.Plugin
import com.google.inject.Inject
import dev.divinegenesis.soulbound.commands.BaseCommand
import dev.divinegenesis.soulbound.customdata.Data
import org.apache.logging.log4j.LogManager
import org.spongepowered.api.Engine
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.*
import org.spongepowered.plugin.PluginContainer


@Plugin("soulbound")
class Soulbound @Inject internal constructor(private val container: PluginContainer) {

    companion object {
        val logger = logger<Soulbound>()
        lateinit var plugin: PluginContainer
    }

    @Listener
    fun onPluginConstruction(event: ConstructPluginEvent) {
        logger.info("Soulbound constructing..")
        //Config
        plugin = container
        Sponge.eventManager().registerListeners(container, EventListener())
        Sponge.eventManager().registerListeners(container,Data(container))




    }

    @Listener
    fun onRegisterCommand(event: RegisterCommandEvent<Command.Parameterized>) {
        logger.info("Registering commands..")

        event.register(
            this.container,
            BaseCommand().helpCommand,"sb"
        )


    }

    @Listener
    fun onServerStart(event:StartedEngineEvent<Engine>) {

    }

    @Listener
    fun onReload(event: RefreshGameEvent) {
        logger.info("Game refreshed...")
    }

    @Listener
    fun onShutdown(event: StoppingEngineEvent<Engine>) {
        logger.info("Server shutting down")
    }

}

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)