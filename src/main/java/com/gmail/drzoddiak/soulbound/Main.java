package com.gmail.drzoddiak.soulbound;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Function;

import static com.gmail.drzoddiak.soulbound.Reference.*;

@Plugin(name = NAME, id = ID, version = VERSION, description = DESC, authors = AUTHORS)
public class Main 
{
    public Main instance;
    private Logger logger; 
    private ConfigurationNode cfg;

    private File defaultCfg;

    private ConfigurationLoader<CommentedConfigurationNode> cfgMgr;

    private Game game;

    @Inject
    public Main(Logger logger, Game game, @DefaultConfig(sharedRoot = false) File defaultCfg, 
    		@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) 
    {
    	this.logger = logger;
        this.game = game;
        this.defaultCfg = defaultCfg;
        this.cfgMgr = cfgMgr;
        instance = this;
    }
    
    @Listener
    public void onPreInit(GamePreInitializationEvent event) 
    {
        getLogger().info("Checking config...");
        try 
        {
            if (!defaultCfg.exists()) 
            {
                getLogger().info("Config not yet created... Don't worry, we got that covered!");
                getLogger().info("Creating config...");
                defaultCfg.createNewFile();
                this.cfg = getCfgMgr().load();
                this.cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
                this.cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
                this.cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
                this.cfg.getNode("Modules", "Permission-check", "Use-Enabled").setValue(false);
                this.cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").setValue(false);
                this.cfg.getNode("Modules", "Permission-check", "Equip-Enabled").setValue(false);
                this.cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").setValue(false);
                getLogger().info("Config created.");
                getCfgMgr().save(cfg);
            }
            
            	this.cfg = getCfgMgr().load();	
            	Function<Object, String> stringTransformer = new Function<Object, String>()
            	{
					@Override
					public String apply(Object input)
					{
						if(input instanceof String)
							return (String) input;
						else
							return null;
					}
				};
            	
				getLogger().info("Saving config data into variables!");
				
				if(this.cfg.getNode("Bind Upon Use").isVirtual())
					this.cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
				sb_use = this.cfg.getNode("Bind Upon Use").getList(stringTransformer);
				
				if(this.cfg.getNode("Bind Upon Pickup").isVirtual())
					this.cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
				sb_pickup = this.cfg.getNode("Bind Upon Pickup").getList(stringTransformer);
            	
            	if(this.cfg.getNode("Bind Upon Equip").isVirtual())
            		this.cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
            	sb_equip = this.cfg.getNode("Bind Upon Equip").getList(stringTransformer);
            	
            	if(this.cfg.getNode("Modules", "Permission-check", "Use-Enabled").isVirtual())
            		this.cfg.getNode("Modules", "Permission-check", "Use-Enabled").setValue(false);
            	use_perm = this.cfg.getNode("Modules", "Permission", "Use-Enabled").getBoolean();
            	
            	if(this.cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").isVirtual())
            		this.cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").setValue(false);
            	pickup_perm = this.cfg.getNode("Modules", "Permission", "Pickup-Enabled").getBoolean();
            	
            	if(this.cfg.getNode("Modules", "Permission-check", "Equip-Enabled").isVirtual())
            		this.cfg.getNode("Modules", "Permission-check", "Equip-Enabled").setValue(false);
            	equip_perm = this.cfg.getNode("Modules", "Permission", "Equip-Enabled").getBoolean();
            	
            	if(this.cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").isVirtual())
            		this.cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").setValue(false);
            	keep_perm = this.cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").getBoolean();
            	getCfgMgr().save(cfg);
            	getLogger().info("Yay! data was saved :D");
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }     
    }

    @Listener
    public void onInit(GameInitializationEvent event) 
    {
        getLogger().info("Starting plugin...");
        Sponge.getEventManager().registerListeners(this, new EventListener());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) 
    {
        this.logger.info("Hello world!");
    }

    public ConfigurationLoader<CommentedConfigurationNode> getCfgMgr() 
    {
        return cfgMgr;
    }

    public Logger getLogger() 
    {
        return logger;
    }

    public ConfigurationNode getCfg() 
    {
        return cfg;
    }

    public Game getGame() 
    {
        return game;
    }
}
