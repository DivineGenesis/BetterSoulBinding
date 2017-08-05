package com.gmail.drzoddiak.soulbound;

import static com.gmail.drzoddiak.soulbound.Reference.equip_perm;
import static com.gmail.drzoddiak.soulbound.Reference.keep_perm;
import static com.gmail.drzoddiak.soulbound.Reference.pickup_perm;
import static com.gmail.drzoddiak.soulbound.Reference.sb_equip;
import static com.gmail.drzoddiak.soulbound.Reference.sb_pickup;
import static com.gmail.drzoddiak.soulbound.Reference.sb_use;
import static com.gmail.drzoddiak.soulbound.Reference.use_perm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.item.ItemTypes;

import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class SBConfig 
{
    public Main instance;
    private Logger logger; 
    private static ConfigurationNode cfg;

    private File defaultCfg;

    private static ConfigurationLoader<CommentedConfigurationNode> cfgMgr;

    private Game game;
    
    @Inject
    public SBConfig(Logger logger, Game game, @DefaultConfig(sharedRoot = false) File defaultCfg, 
    		@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr, Main instance) 
    {
    	this.logger = logger;
        this.game = game;
        this.defaultCfg = defaultCfg;
        SBConfig.cfgMgr = cfgMgr;
        this.instance = instance;
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
    
    public static void savetoFile()
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
    
    public void configCheck()
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

}
