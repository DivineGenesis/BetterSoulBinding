package dev.divinegenesis.soulbound

import org.apache.logging.log4j.Logger
import org.spongepowered.plugin.jvm.Plugin
import com.google.inject.Inject
import dev.divinegenesis.soulbound.customdata.Data
import org.spongepowered.api.Engine
import org.spongepowered.api.Server
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.*
import org.spongepowered.plugin.PluginContainer


@Plugin("soulbound")
data class Soulbound @Inject internal constructor(private val container: PluginContainer, private val logger: Logger) {

    @Listener
    fun onPluginConstruction(event: ConstructPluginEvent) {
        logger.info("Soulbound constructing..")
        //Config

        Sponge.eventManager().registerListeners(container, Data(container,logger))
    }

    //@Listener
    //fun onRegisterCommand(event: RegisterCommandEvent<>)


    @Listener
    fun onReload(event: RefreshGameEvent) {
        logger.info("Game refreshed...")
    }

    @Listener
    fun onShutdown(event: StoppingEngineEvent<Engine>) {
        logger.info("Server shutting down")
    }

}