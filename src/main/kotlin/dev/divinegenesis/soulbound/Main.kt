package dev.divinegenesis.soulbound

import org.apache.logging.log4j.Logger
import org.spongepowered.plugin.jvm.Plugin
import com.google.inject.Inject
import org.spongepowered.api.Server
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.StartedEngineEvent


@Plugin("soulbound")
class Main @Inject constructor(
    private val logger: Logger
) {

    @Listener
    fun onStart (event: StartedEngineEvent<Server>) {
        logger.info("Server has started")
    }

    companion object {
        lateinit var instance: Main
            private set
    }

    init {
        instance = this
    }
}