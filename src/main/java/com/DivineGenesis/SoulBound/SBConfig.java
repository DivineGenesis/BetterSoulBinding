
 package com.DivineGenesis.SoulBound;

import static com.DivineGenesis.SoulBound.Reference.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.spongepowered.api.Game;

import org.spongepowered.api.item.ItemType;

import org.spongepowered.api.item.ItemTypes;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class SBConfig 
{
    private Logger logger;
    private static ConfigurationNode cfg;
    private File defaultCfg;
    private static ConfigurationLoader<CommentedConfigurationNode> cfgMgr;
    private Game game;
    
    SBConfig(Logger logger, Game game, File defaultCfg, ConfigurationLoader<CommentedConfigurationNode> cfgMgr)
    {
    	this.logger = logger;
        this.game = game;
        this.defaultCfg = defaultCfg;
        SBConfig.cfgMgr = cfgMgr;
    }
    
    private static ConfigurationLoader<CommentedConfigurationNode> getCfgMgr()
    {
        return cfgMgr;
    }

    Logger getLogger()
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


    private static String use = "Bind Upon Use";
    private static String pickup = "Bind Upon Pickup";
    private static String craft = "Bind Upon Craft";
    private static String itemUse = ItemTypes.DIAMOND_SWORD.getId();
    private static String itemPickup = ItemTypes.COAL.getId();
    private static String itemCraft = ItemTypes.DIAMOND_HELMET.getId();



    static void saveToFile()
    {
    	try 
    	{
			cfg = getCfgMgr().load();

			cfg.getNode(use).setValue(Reference.sb_use);
			cfg.getNode(pickup).setValue(Reference.sb_pickup);
			cfg.getNode(craft).setValue(Reference.sb_craft);

			getCfgMgr().save(cfg);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    void configCheck()
    {
    	getLogger().info("Reloading...\nChecking config...");
        try 
        {
        	cfg = getCfgMgr().load();



            String useEn = "Use-Enabled";
            String pickupEn = "Pickup-Enabled";
            String keepEn = "KeepUponDeath-Enabled";
            String mod = "Modules";
            String permCheck = "Permission-Check";
            String craftEn = "Craft-Enabled";


            if (!defaultCfg.exists())

            {
                getLogger().info("Config not yet created... Don't worry, we got that covered!\nCreating config...");
                defaultCfg.createNewFile();
                cfg = getCfgMgr().load();


                cfg.getNode(use).setValue(new ArrayList<String>(){{add(itemUse);}});
                cfg.getNode(pickup).setValue(new ArrayList<String>(){{add(itemPickup);}});
                cfg.getNode(craft).setValue(new ArrayList<String>(){{add(itemCraft);}});


                cfg.getNode(mod, permCheck, useEn).setValue(false);
                cfg.getNode(mod, permCheck, pickupEn).setValue(false);
                cfg.getNode(mod, permCheck, craftEn).setValue(false);
                cfg.getNode(mod, permCheck, keepEn).setValue(false);



                getLogger().info("Config created.");
                getCfgMgr().save(cfg);
            }
            
				getLogger().info("Saving config data into variables!");
				

				if(cfg.getNode(use).isVirtual())
					cfg.getNode(use).setValue(new ArrayList<String>(){{add(itemUse);}});
				sb_use = cfg.getNode(use).getList(TypeToken.of(String.class));
				
				if(cfg.getNode(pickup).isVirtual())
					cfg.getNode(pickup).setValue(new ArrayList<String>(){{add(itemPickup);}});
				sb_pickup = cfg.getNode(pickup).getList(TypeToken.of(String.class));

            if(cfg.getNode(craft).isVirtual())
                cfg.getNode(craft).setValue(new ArrayList<String>(){{add(itemCraft);}});
            sb_craft = cfg.getNode(craft).getList(TypeToken.of(String.class));





            	if(cfg.getNode(mod, permCheck, useEn).isVirtual())
            		cfg.getNode(mod, permCheck, useEn).setValue(false);
            	use_perm = cfg.getNode(mod, permCheck, useEn).getBoolean();
            	
            	if(cfg.getNode(mod, permCheck, pickupEn).isVirtual())
            		cfg.getNode(mod, permCheck, pickupEn).setValue(false);
            	pickup_perm = cfg.getNode(mod, permCheck, pickupEn).getBoolean();

            if(cfg.getNode(mod, permCheck, craftEn).isVirtual())
                cfg.getNode(mod, permCheck, craftEn).setValue(false);
            craft_perm = cfg.getNode(mod, permCheck, craftEn).getBoolean();

            	if(cfg.getNode(mod, permCheck, keepEn).isVirtual())
            		cfg.getNode(mod, permCheck, keepEn).setValue(false);
            	keep_perm = cfg.getNode(mod, permCheck, keepEn).getBoolean();

            	
            	getCfgMgr().save(cfg);
            	getLogger().info("Yay! Data was saved :D");
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }

}
