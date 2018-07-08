package com.DivineGenesis.SoulBound;

import java.io.File;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import com.DivineGenesis.SoulBound.Messages.Console;
import com.DivineGenesis.SoulBound.EventListeners.EventListener;
import com.DivineGenesis.SoulBound.config.SBConfig;

@Plugin(name = Main.NAME, id = Main.ID, version = Main.VERSION, description = Main.DESC, authors = Main.AUTHORS)
public class Main {
	
	//@Plugin params
	public static final String NAME = "Soulbound";
	public static final String ID = "soulbound";
	public static final String VERSION = "0.10.3";
	public static final String DESC = "Binds items to the users soul!";
	public static final String AUTHORS = "DrZoddiak & Burpingdog1";
	
    private static SBConfig config;

    @Inject
    public Main (Logger logger,@DefaultConfig (sharedRoot = false) File defaultCfg,
                 @DefaultConfig (sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) {
        config = new SBConfig(logger,defaultCfg,cfgMgr);
    }

    @Listener
    public void onPreInit (GamePreInitializationEvent event) {
        config.configCheck(false);
    }

    @Listener
    public void onReload (GameReloadEvent event) {
        config.configCheck(true);
    }

    @Listener
    public void onInit (GameInitializationEvent event) {
        config.getLogger().info(Console.REGISTERING);
        Sponge.getEventManager().registerListeners(this,new EventListener());
        Sponge.getCommandManager().register(this,new CmdLoader().sb,"sb");
    }

    @Listener
    public void onServerStart (GameStartedServerEvent event) {
        config.getLogger().info(Console.LOADED);
    }
}