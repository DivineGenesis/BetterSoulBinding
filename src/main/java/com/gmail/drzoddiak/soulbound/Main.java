package com.gmail.drzoddiak.soulbound;

import com.gmail.drzoddiak.soulbound.commands.CmdLoader;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import static com.gmail.drzoddiak.soulbound.Reference.*;

@Plugin(name = NAME, id = ID, version = VERSION, description = DESC, authors = AUTHORS)
public class Main 
{
    public Main instance;
    private Logger logger; 
    private static ConfigurationNode cfg;

    private File defaultCfg;

    private static ConfigurationLoader<CommentedConfigurationNode> cfgMgr;

    private Game game;

    @Inject
    public Main(Logger logger, Game game, @DefaultConfig(sharedRoot = false) File defaultCfg, 
    		@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) 
    {
    	this.logger = logger;
        this.game = game;
        this.defaultCfg = defaultCfg;
        Main.cfgMgr = cfgMgr;
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
                cfg = getCfgMgr().load();
                cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
                cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
                cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
                cfg.getNode("Modules", "Permission-check", "Use-Enabled").setValue(false);
                cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").setValue(false);
                cfg.getNode("Modules", "Permission-check", "Equip-Enabled").setValue(false);
                cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").setValue(false);
                getLogger().info("Config created.");
                getCfgMgr().save(cfg);
            }
            
            	cfg = getCfgMgr().load();	
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
				
				if(cfg.getNode("Bind Upon Use").isVirtual())
					cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
				sb_use = cfg.getNode("Bind Upon Use").getList(stringTransformer);
				
				if(cfg.getNode("Bind Upon Pickup").isVirtual())
					cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
				sb_pickup = cfg.getNode("Bind Upon Pickup").getList(stringTransformer);
            	
            	if(cfg.getNode("Bind Upon Equip").isVirtual())
            		cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
            	sb_equip = cfg.getNode("Bind Upon Equip").getList(stringTransformer);
            	
            	if(cfg.getNode("Modules", "Permission-check", "Use-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "Use-Enabled").setValue(false);
            	use_perm = cfg.getNode("Modules", "Permission", "Use-Enabled").getBoolean();
            	
            	if(cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").setValue(false);
            	pickup_perm = cfg.getNode("Modules", "Permission", "Pickup-Enabled").getBoolean();
            	
            	if(cfg.getNode("Modules", "Permission-check", "Equip-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "Equip-Enabled").setValue(false);
            	equip_perm = cfg.getNode("Modules", "Permission", "Equip-Enabled").getBoolean();
            	
            	if(cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").setValue(false);
            	keep_perm = cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").getBoolean();
            	
            	getCfgMgr().save(cfg);
            	getLogger().info("Yay! data was saved :D");
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }     
    }
    
    @Listener
    public void onReload(GameReloadEvent event)
    {
    	getLogger().info("Reloading...");
    	getLogger().info("Checking config...");
        try 
        {
            if (!defaultCfg.exists()) 
            {
                getLogger().info("Config not yet created... Don't worry, we got that covered!");
                getLogger().info("Creating config...");
                defaultCfg.createNewFile();
                cfg = getCfgMgr().load();
                cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
                cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
                cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
                cfg.getNode("Modules", "Permission-check", "Use-Enabled").setValue(false);
                cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").setValue(false);
                cfg.getNode("Modules", "Permission-check", "Equip-Enabled").setValue(false);
                cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").setValue(false);
                getLogger().info("Config created.");
                getCfgMgr().save(cfg);
            }
            
            	cfg = getCfgMgr().load();	
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
				
				if(cfg.getNode("Bind Upon Use").isVirtual())
					cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
				sb_use = cfg.getNode("Bind Upon Use").getList(stringTransformer);
				
				if(cfg.getNode("Bind Upon Pickup").isVirtual())
					cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
				sb_pickup = cfg.getNode("Bind Upon Pickup").getList(stringTransformer);
            	
            	if(cfg.getNode("Bind Upon Equip").isVirtual())
            		cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
            	sb_equip = cfg.getNode("Bind Upon Equip").getList(stringTransformer);
            	
            	if(cfg.getNode("Modules", "Permission-check", "Use-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "Use-Enabled").setValue(false);
            	use_perm = cfg.getNode("Modules", "Permission", "Use-Enabled").getBoolean();
            	
            	if(cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "Pickup-Enabled").setValue(false);
            	pickup_perm = cfg.getNode("Modules", "Permission", "Pickup-Enabled").getBoolean();
            	
            	if(cfg.getNode("Modules", "Permission-check", "Equip-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "Equip-Enabled").setValue(false);
            	equip_perm = cfg.getNode("Modules", "Permission", "Equip-Enabled").getBoolean();
            	
            	if(cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").isVirtual())
            		cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").setValue(false);
            	keep_perm = cfg.getNode("Modules", "Permission-check", "KeepUponDeath-Enabled").getBoolean();
            	
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
        getLogger().info("Registering Events & Commands...");
        Sponge.getEventManager().registerListeners(this, new EventListener());
        Sponge.getCommandManager().register(this, new CmdLoader().sb, "sb");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) 
    {
        logger.info("Hello world!");
    }

    public static ConfigurationLoader<CommentedConfigurationNode> getCfgMgr() 
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
    
    public static void saveFile()
    {
    	try 
    	{
			cfg = getCfgMgr().load();
			cfg.getNode("Bind Upon Use").setValue(Reference.sb_use);
			cfg.getNode("Bind Upon Equip").setValue(Reference.sb_equip);
			cfg.getNode("Bind Upon Pickup").setValue(Reference.sb_pickup);
			getCfgMgr().save(cfg);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
}
