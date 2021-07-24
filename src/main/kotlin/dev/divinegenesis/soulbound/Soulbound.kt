package dev.divinegenesis.soulbound

import org.apache.logging.log4j.Logger
import org.spongepowered.plugin.jvm.Plugin
import com.google.inject.Inject
import dev.divinegenesis.soulbound.commands.BaseCommand
import dev.divinegenesis.soulbound.config.Config
import dev.divinegenesis.soulbound.customdata.Data
import dev.divinegenesis.soulbound.storage.DataStack
import dev.divinegenesis.soulbound.storage.SqliteDatabase
import org.apache.logging.log4j.LogManager
import org.spongepowered.api.Engine
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.Command
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.*
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.registry.RegistryTypes
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.reference.ConfigurationReference
import org.spongepowered.configurate.reference.ValueReference
import org.spongepowered.plugin.PluginContainer
import java.nio.file.Path


@Plugin("soulbound")
class Soulbound @Inject internal constructor(
    private val container: PluginContainer,
    @DefaultConfig(sharedRoot = false) val reference: ConfigurationReference<CommentedConfigurationNode>,
    @ConfigDir(sharedRoot = false) val configDir: Path
) {

    companion object {
        val logger = logger<Soulbound>()
        lateinit var plugin: PluginContainer
        lateinit var config: ValueReference<Config, CommentedConfigurationNode>
        lateinit var configDir: Path
        lateinit var database: Map<String, DataStack>
    }

    init {
        plugin = container
        config = reference.referenceTo(Config::class.java)
        Soulbound.configDir = configDir
        database = SqliteDatabase().loadData()

        logger.info("Soulbound constructing..")
        try {
            this.reference.save()
        } catch (e: ConfigurateException) {
            logger.error("Unable to load configuration", e)
        }
        Sponge.eventManager().registerListeners(container, EventListener())
        Sponge.eventManager().registerListeners(container, Data(container))
    }

    @Listener
    fun onPluginConstruction(event: ConstructPluginEvent) {
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
}

fun ItemStack.getID() = Sponge.game().registries().registry(RegistryTypes.ITEM_TYPE).valueKey(this.type()).asString()

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)