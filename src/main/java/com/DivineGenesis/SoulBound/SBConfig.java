package com.DivineGenesis.SoulBound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.item.ItemTypes;


import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import static com.DivineGenesis.SoulBound.Reference.*;

public class SBConfig {
    private Logger logger;
    private static ConfigurationNode cfg;
    private File defaultCfg;
    private static ConfigurationLoader<CommentedConfigurationNode> cfgMgr;
    private Game game;
    
    SBConfig(Logger logger, Game game, File defaultCfg, ConfigurationLoader<CommentedConfigurationNode> cfgMgr) {
        this.logger = logger;
        this.game = game;
        this.defaultCfg = defaultCfg;
        SBConfig.cfgMgr = cfgMgr;
    }
    
    private static ConfigurationLoader<CommentedConfigurationNode> getCfgMgr() {
        return cfgMgr;
    }

    Logger getLogger() {
        return logger;
    }

    public ConfigurationNode getCfg() {
        return cfg;
    }

    public Game getGame() {
        return game;
    }

    private static String use = "Bind Upon Use";
    private static String pickup = "Bind Upon Pickup";
    private static String craft = "Bind Upon Craft";
    private static String itemUse = ItemTypes.DIAMOND_SWORD.getId();
    private static String itemPickup = ItemTypes.COAL.getId();
    private static String itemCraft = ItemTypes.DIAMOND_HELMET.getId();


    static void saveToFile() {
    	try {
			cfg = getCfgMgr().load();

			cfg.getNode(use).setValue(Reference.sb_use);
			cfg.getNode(pickup).setValue(Reference.sb_pickup);
			cfg.getNode(craft).setValue(Reference.sb_craft);

			getCfgMgr().save(cfg);
    	}
    	catch (IOException e) {
			e.printStackTrace();
    	}
    }

    void configCheck() {
    	getLogger().info("Reloading...\nChecking config...");
        try {
        	cfg = getCfgMgr().load();


            String useEn = "Use-Enabled";
            String pickupEn = "Pickup-Enabled";
            String keepEn = "KeepUponDeath-Enabled";
            String mod = "Modules";
            String permCheck = "Permission-Check";
            String craftEn = "Craft-Enabled";

            List<String> bindTypes = new ArrayList<>();
            bindTypes.add(use);
            bindTypes.add(pickup);
            bindTypes.add(craft);

            List<List<String>> sbType = new ArrayList<>();
            sbType.add(sb_use);
            sbType.add(sb_pickup);
            sbType.add(sb_craft);

            List<String> craftTypes = new ArrayList<>();
            bindTypes.add(itemUse);
            bindTypes.add(itemPickup);
            bindTypes.add(itemCraft);

            List<String> enableType= new ArrayList<>();
            enableType.add(useEn);
            enableType.add(pickupEn);
            enableType.add(craftEn);
            enableType.add(keepEn);

            List<Boolean> boolType = new ArrayList<>();
            boolType.add(use_perm);
            boolType.add(pickup_perm);
            boolType.add(craft_perm);
            boolType.add(keep_perm);

            if (!defaultCfg.exists()) {
                getLogger().info("Config not yet created... Don't worry, we got that covered!\nCreating config...");
                defaultCfg.createNewFile();
                cfg = getCfgMgr().load();



                for(int i=0; i < bindTypes.size() ; i++){
                    int finalI = i;
                    cfg.getNode(bindTypes.get(i)).setValue(new ArrayList<String>(){{add(craftTypes.get(finalI));}});
                }

                for (String anEnableType : enableType) {
                    cfg.getNode(mod, permCheck, anEnableType).setValue(false);
                }

                getLogger().info("Config created.");
                getCfgMgr().save(cfg);
            }
				getLogger().info("Saving config data into variables!");

                for(int i=0; i < sbType.size(); i++){
                    List<String> sbTypeList = sbType.get(i);
                    Boolean boolTypeList = boolType.get(i);
                      if(cfg.getNode(bindTypes.get(i)).isVirtual()) {
                          int finalI = i;

                          cfg.getNode(bindTypes.get(i)).setValue(new ArrayList<String>(){{add(craftTypes.get(finalI)); }});
                      }
                    sbTypeList = cfg.getNode(bindTypes.get(i)).getList(TypeToken.of(String.class));

                    if(cfg.getNode(mod, permCheck, enableType.get(i)).isVirtual())
                        cfg.getNode(mod, permCheck, enableType.get(i)).setValue(false);
                    boolTypeList = cfg.getNode(mod, permCheck, enableType.get(i)).getBoolean();
                }
            	getCfgMgr().save(cfg);
            	getLogger().info("Yay! Data was saved :D");
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
