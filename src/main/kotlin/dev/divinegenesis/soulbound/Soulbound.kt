package dev.divinegenesis.soulbound

import org.apache.logging.log4j.Logger
import com.google.inject.Inject
import dev.divinegenesis.soulbound.commands.BaseCommand
import dev.divinegenesis.soulbound.config.Config
import dev.divinegenesis.soulbound.customdata.Data
import dev.divinegenesis.soulbound.customdata.DataStack
import dev.divinegenesis.soulbound.storage.SqliteDatabase
import org.apache.logging.log4j.LogManager
import org.spongepowered.api.Engine
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.*
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.reference.ConfigurationReference
import org.spongepowered.configurate.reference.ValueReference
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin
import java.nio.file.Path

@Suppress("UNUSED_PARAMETER")
@Plugin("soulbound")
class Soulbound @Inject internal constructor(
    private val container: PluginContainer,
    @DefaultConfig(sharedRoot = false) val reference: ConfigurationReference<CommentedConfigurationNode>,
    @ConfigDir(sharedRoot = false) val path: Path
) {

    companion object {
        lateinit var instance : Soulbound
    }

    init {
        instance = this
    }

    lateinit var database : Map<String, DataStack>
    val config: ValueReference<Config, CommentedConfigurationNode> = reference.referenceTo(Config::class.java)
    val configDir : Path = this.path
    val logger = logger()
    val pluginContainer = this.container

    @Listener
    fun onPluginConstruction(event: ConstructPluginEvent) {
        database = SqliteDatabase().loadData()

        logger.info("Soulbound constructing..")
        try {
            this.reference.save()
        } catch (e: ConfigurateException) {
            logger.error("Unable to load configuration", e)
        }
        Sponge.eventManager().registerListeners(this.container, EventListener())
        Sponge.eventManager().registerListeners(this.container, Data())
    }

    @Listener
    fun onRegisterCommand(event: RegisterCommandEvent<Command.Parameterized>) {
        logger.info("Registering commands..")

        event.register(
            this.container,
            BaseCommand().helpCommand, "sb"
        )
    }

    @Listener
    fun onServerStart(event: StartedEngineEvent<Engine>) {
        logger.info("Data loaded: ${database.size} entries")
    }

    @Listener
    fun onReload(event: RefreshGameEvent) {
        logger.info("Game refreshed...")
    }

    @Listener
    fun onShutdown(event: StoppingEngineEvent<Engine>) {
        logger.info("Server shutting down")
    }

    private fun logger(): Logger = LogManager.getLogger(Soulbound::class.java)
}