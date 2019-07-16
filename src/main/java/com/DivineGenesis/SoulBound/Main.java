package com.DivineGenesis.SoulBound;

import com.DivineGenesis.SoulBound.eventlisteners.EventListener;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

import static com.DivineGenesis.SoulBound.Reference.*;


@Plugin (name = NAME, id = ID, version = VERSION, description = DESC, authors = AUTHORS)
public class Main {

    private static SBConfig config;


    private DataRegistration<IdentityData, IdentityData.Immutable> IDENTITY_DATA_REGISTRATION;

    @Inject
    public Main (Logger logger,Game game,@DefaultConfig (sharedRoot = false) File defaultCfg,@DefaultConfig (sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) {

        config = new SBConfig(logger,game,defaultCfg,cfgMgr);
    }


    @Listener
    public void onPreInit (GamePreInitializationEvent event) {

        config.configCheck();

        this.IDENTITY_DATA_REGISTRATION = DataRegistration.builder().dataClass(IdentityData.class).immutableClass(IdentityData.Immutable.class).dataImplementation(IdentityData.class).immutableImplementation(IdentityData.Immutable.class).id("identity").builder(new IdentityData.Builder()).build();
    }

    @Listener
    public void onReload (GameReloadEvent event) {

        config.configCheck();
    }

    @Listener
    public void onInit (GameInitializationEvent event) {

        config.getLogger().info("Registering Events & Commands...");
        Sponge.getEventManager().registerListeners(this,new EventListener());
        Sponge.getCommandManager().register(this,new CmdLoader().sb,"sb");

    }

    @Listener
    public void onKeyRegister (GameRegistryEvent.Register<Key<?>> event) {

        new IdentityKeys();

    }

    @Listener
    public void onServerStart (GameStartedServerEvent event) {

        config.getLogger().info(ID + " has loaded :)");

    }
}
