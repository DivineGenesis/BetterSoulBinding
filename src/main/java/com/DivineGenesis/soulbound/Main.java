package com.DivineGenesis.soulbound;

import com.DivineGenesis.soulbound.data.identity.IdentityData;
import com.DivineGenesis.soulbound.data.identity.ImmutableIdentityData;
import com.DivineGenesis.soulbound.data.identity.Keys;
import com.DivineGenesis.soulbound.data.identity.impl.IdentityDataBuilder;
import com.DivineGenesis.soulbound.data.identity.impl.IdentityDataImpl;
import com.DivineGenesis.soulbound.data.identity.impl.ImmutableIdentityDataImpl;
import com.DivineGenesis.soulbound.eventlisteners.EventListener;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import java.io.File;
import java.util.UUID;

import static com.DivineGenesis.soulbound.Reference.*;


@Plugin (name = NAME, id = ID, version = VERSION, description = DESC, authors = AUTHORS)
public class Main {

    private static SBConfig config;



    private DataRegistration<IdentityData, ImmutableIdentityData> IDENTITY_DATA_REGISTRATION;

    @Inject
    public Main (Logger logger,Game game,@DefaultConfig (sharedRoot = false) File defaultCfg,@DefaultConfig (sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) {

        config = new SBConfig(logger,game,defaultCfg,cfgMgr);
    }

    @Inject
    private PluginContainer container;

    @Listener
    public void onPreInit (GamePreInitializationEvent event) {

        config.configCheck();
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
    public void onDataRegister (GameRegistryEvent.Register<DataRegistration<?, ?>> event) {

        this.IDENTITY_DATA_REGISTRATION = DataRegistration.builder()
                .dataClass(IdentityData.class)
                .immutableClass(ImmutableIdentityData.class)
                .dataImplementation(IdentityDataImpl.class)
                .immutableImplementation(ImmutableIdentityDataImpl.class)
                .builder(new IdentityDataBuilder())
                .id("identity")
                .build();

    }

    @Listener
    public void onKeyRegister (GameRegistryEvent.Register<Key<?>> event) {

        new Keys();

    }

    @Listener
    public void onServerStart (GameStartedServerEvent event) {

        config.getLogger().info("Hello world!");

    }

}