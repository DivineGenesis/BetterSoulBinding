package com.DivineGenesis.SoulBound;


import com.DivineGenesis.SoulBound.config.ConfigLoader;
import com.DivineGenesis.SoulBound.config.MessagesConfig;
import com.DivineGenesis.SoulBound.config.SBConfig;
import com.DivineGenesis.SoulBound.data.IdentityData;
import com.DivineGenesis.SoulBound.data.IdentityKeys;
import com.DivineGenesis.SoulBound.eventlisteners.EventListener;
import com.DivineGenesis.SoulBound.eventlisteners.NucleusEventListener;
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

import java.io.File;

import static com.DivineGenesis.SoulBound.Reference.*;


@Plugin (name = NAME, id = ID, version = VERSION, description = DESC, authors = AUTHORS, dependencies = {@Dependency (id = "nucleus", version = "1.14.0", optional = true)})
public class Main {

    private static Main instance;

    private com.DivineGenesis.SoulBound.config.SBConfig SBConfig;
    private com.DivineGenesis.SoulBound.config.MessagesConfig MessagesConfig;
    private final GuiceObjectMapperFactory factory;
    private final Logger logger;
    private final File configDir;
    private ConfigLoader cfgLoader;

    private DataRegistration<IdentityData, IdentityData.Immutable> IDENTITY_DATA_REGISTRATION;

    @Inject
    public Main (Logger logger, @ConfigDir (sharedRoot = false) File configDir, GuiceObjectMapperFactory factory) {

        this.logger = logger;
        this.configDir = configDir;
        this.factory = factory;
        instance = this;
    }

    public MessagesConfig getMessagesConfig () {

        return MessagesConfig;
    }

    public SBConfig getSBConfig () {

        return SBConfig;
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
            MessagesConfig = cfgLoader.getMessagesConfig();
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
            MessagesConfig = cfgLoader.getMessagesConfig();
        }
    }

    @Listener
    public void onShutdown (GameStoppingServerEvent event) {

        saveConfig();
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