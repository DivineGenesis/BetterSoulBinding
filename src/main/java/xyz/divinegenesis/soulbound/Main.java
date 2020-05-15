package xyz.divinegenesis.soulbound;


import com.google.inject.Inject;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import xyz.divinegenesis.soulbound.config.ConfigLoader;
import xyz.divinegenesis.soulbound.config.MessagesConfig;
import xyz.divinegenesis.soulbound.config.SBConfig;
import xyz.divinegenesis.soulbound.data.IdentityData;
import xyz.divinegenesis.soulbound.data.IdentityKeys;
import xyz.divinegenesis.soulbound.eventlisteners.EventListener;
import xyz.divinegenesis.soulbound.eventlisteners.NucleusEventListener;

import java.io.File;


@Plugin (name = Reference.NAME, id = Reference.ID, version = Reference.VERSION, description = Reference.DESCRIPTION, authors = {"Jesse McKee", "Burpingdog1"}, dependencies = {@Dependency (id = "nucleus", version = Reference.NUCLEUS_VERSION, optional = true)})
public class Main {

    private static Main instance;
    private static xyz.divinegenesis.soulbound.config.SBConfig SBConfig;
    private final GuiceObjectMapperFactory factory;
    private final Logger logger;
    private final File configDir;
    private MessagesConfig messagesConfig;
    private ConfigLoader cfgLoader;
    private DataRegistration<IdentityData, IdentityData.Immutable> IDENTITY_DATA_REGISTRATION;

    @Inject
    public Main (Logger logger, @ConfigDir (sharedRoot = false) File configDir, GuiceObjectMapperFactory factory) {

        this.logger = logger;
        this.configDir = configDir;
        this.factory = factory;
        instance = this;
    }

    @Listener
    public void onKeyRegister (GameRegistryEvent.Register<Key<?>> event) {

        logger.info("Registering Data Key...");
        event.register(IdentityKeys.IDENTITY);
    }

    @Listener
    public void onPreInit (GamePreInitializationEvent event) {

        logger.info("Setting up configs...");
        cfgLoader = new ConfigLoader(this);

        if (cfgLoader.loadConfig()) {
            SBConfig = cfgLoader.getSBConfig();
        }
        if (cfgLoader.loadMessages()) {
            messagesConfig = cfgLoader.getMessagesConfig();
        }

        logger.info("Registering custom data...");
        this.IDENTITY_DATA_REGISTRATION = DataRegistration.builder()
                .dataClass(IdentityData.class)
                .immutableClass(IdentityData.Immutable.class)
                .dataImplementation(IdentityData.class)
                .immutableImplementation(IdentityData.Immutable.class)
                .id("identity")
                .builder(new IdentityData.Builder())
                .build();
    }

    @Listener
    public void onInit (GameInitializationEvent event) {

        logger.info("Registering events...");
        Sponge.getEventManager().registerListeners(this, new EventListener());

        logger.info("Checking if Nucleus is installed for kit redemption support");
        if (Sponge.getPluginManager().isLoaded("nucleus")) {
            logger.info("Nucleus found, registering Nucleus events...");
            Sponge.getEventManager().registerListeners(this, new NucleusEventListener());
        } else {
            logger.info("Nucleus not found, Kit redemption support will be disabled!");
        }

    }

    @Listener
    public void OnServerStarting (GameStartingServerEvent event) {

        logger.info("Registering Commands...");
        Sponge.getCommandManager().register(this, new CmdLoader().sb, "sb");


    }

    @Listener
    public void onReload (GameReloadEvent event) {

        cfgLoader = new ConfigLoader(this);

        if (cfgLoader.loadConfig()) {
            SBConfig = cfgLoader.getSBConfig();
        }
        if (cfgLoader.loadMessages()) {
            messagesConfig = cfgLoader.getMessagesConfig();
        }
    }

    @Listener
    public void onShutdown (GameStoppingServerEvent event) {

        saveConfig();
    }

    public MessagesConfig getMessagesConfig () {

        return messagesConfig;
    }

    public SBConfig getSBConfig () {

        return SBConfig;
    }

    public void saveConfig () {

        cfgLoader.saveConfig(SBConfig);
    }

    public static Main getInstance () {

        return instance;
    }

    public File getConfigDir () {

        return configDir;
    }

    public Logger getLogger () {

        return logger;
    }

    public GuiceObjectMapperFactory getFactory () {

        return factory;
    }
}