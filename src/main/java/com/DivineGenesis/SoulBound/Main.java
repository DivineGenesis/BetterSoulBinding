package com.divinegenesis.soulbound;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.divinegenesis.soulbound.data.ImmutableSoulboundData;
import com.divinegenesis.soulbound.data.SoulboundData;
import com.divinegenesis.soulbound.data.SoulboundDataBuilder;
import com.divinegenesis.soulbound.data.SoulboundKeys;
import com.divinegenesis.soulbound.events.EventListener;
import com.google.inject.Inject;

import java.io.File;
import static com.divinegenesis.soulbound.Reference.*;

@Plugin(name = NAME, id = ID, version = VERSION, description = DESC, authors = AUTHORS)
public class Main 
{
	private static SBConfig config;
    
	@Inject
	private PluginContainer instance;
	
    @Inject
    public Main(Logger logger, Game game, @DefaultConfig(sharedRoot = false) File defaultCfg, 
    		@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) 
    {
    	config = new SBConfig(logger, game, defaultCfg, cfgMgr);
    }
	
    @Listener
    public void onPreInit(GamePreInitializationEvent event) 
    {
    	config.configCheck();
    	
    	SoulboundKeys.init();
    	 DataRegistration.<SoulboundData, ImmutableSoulboundData>builder()
         .dataClass(SoulboundData.class)
         .immutableClass(ImmutableSoulboundData.class)
         .builder(new SoulboundDataBuilder())
         .manipulatorId("soulbound")
         .dataName("Soulbound User Data")
         .buildAndRegister(instance);
    }
    
    @Listener
    public void onReload(GameReloadEvent event)
    {
        config.configCheck();
    }

    @Listener
    public void onInit(GameInitializationEvent event) 
    {
    	config.getLogger().info("Registering Events & Commands...");
        Sponge.getEventManager().registerListeners(this, new EventListener());
        Sponge.getCommandManager().register(this, new CmdLoader().sb, "sb");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) 
    {
    	config.getLogger().info("Hello world!");
    }
}