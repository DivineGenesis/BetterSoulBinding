package dev.divinegenesis.soulbound

import org.apache.logging.log4j.Logger
import org.spongepowered.plugin.jvm.Plugin
import com.google.inject.Inject
import org.spongepowered.api.Server
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.StartedEngineEvent
import org.spongepowered.plugin.PluginContainer


@Plugin("soulbound")
class Soulbound @Inject internal constructor(private val container: PluginContainer, private val logger: Logger) {

    @Listener
    fun onStart (event: StartedEngineEvent<Server>) {
        logger.info("Server has started with a new thing")
    }

}