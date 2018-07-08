package com.DivineGenesis.SoulBound.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.DivineGenesis.SoulBound.Messages.*;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.spongepowered.api.item.ItemTypes;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import static com.DivineGenesis.SoulBound.Messages.Config.*;

public class SBConfig {
	
	//Plugin config data
	public static List<String> sb_pickup = new ArrayList<>();
	public static List<String> sb_use = new ArrayList<>();
	public static List<String> sb_craft = new ArrayList<>();
	public static boolean pickup_perm;
	public static boolean use_perm;
	public static boolean craft_perm;
	public static boolean keep_perm;
	
    private Logger logger;
    private File defaultCfg;
    private static ConfigurationLoader<CommentedConfigurationNode> cfgMgr;
    private static ConfigurationNode cfg;


    public SBConfig (Logger logger,File defaultCfg,ConfigurationLoader<CommentedConfigurationNode> cfgMgr) {
        this.logger = logger;
        this.defaultCfg = defaultCfg;
        SBConfig.cfgMgr = cfgMgr;
    }

    private static ConfigurationLoader<CommentedConfigurationNode> getCfgMgr () {
        return cfgMgr;
    }

    public Logger getLogger () {
        return logger;
    }

    public static void saveToFile () {
        try {
            cfg = getCfgMgr().load();

            cfg.getNode(use).setValue(sb_use);
            cfg.getNode(pickup).setValue(sb_pickup);
            cfg.getNode(craft).setValue(sb_craft);

            getCfgMgr().save(cfg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void configCheck (boolean reload) {
        if(reload) 
        	getLogger().info(Console.RELOAD);
        
        try {
            String itemUse = ItemTypes.DIAMOND_SWORD.getId();
            String itemPickup = ItemTypes.COAL.getId();
            String itemCraft = ItemTypes.DIAMOND_HELMET.getId();

            List<String> bindTypes = new ArrayList<>(Arrays.asList(use,pickup,craft));
            List<String> itemType = new ArrayList<>(Arrays.asList(itemUse,itemPickup,itemCraft));
            List<String> enableType = new ArrayList<>(Arrays.asList(useEn,pickupEn,craftEn,keepEn));
            List<List<String>> sbType = new ArrayList<>(Arrays.asList(sb_use,sb_pickup,sb_craft));
            List<Boolean> boolType = new ArrayList<>(Arrays.asList(use_perm,pickup_perm,craft_perm,keep_perm));

            if (!defaultCfg.exists()) {
                getLogger().info(Console.NO_CONFIG);
                //noinspection ResultOfMethodCallIgnored
                defaultCfg.createNewFile();
                cfg = getCfgMgr().load();
                
                for (String i : bindTypes)
                    cfg.getNode(i).setValue(new ArrayList<String>() {{add(i);}});

                for (String i : enableType)
                    cfg.getNode(mod,permCheck,i).setValue(false);

                getLogger().info(Console.CONFIG_CREATED);
                getCfgMgr().save(cfg);
            }
            getLogger().info(Console.SAVING_VARIABLES);

            for (int i = 0; i < sbType.size(); i++) {
                List<String> sbTypeList = sbType.get(i);
                Boolean boolTypeList = boolType.get(i);
                if (cfg.getNode(bindTypes.get(i)).isVirtual()) {
                    int finalI = i;
                    cfg.getNode(bindTypes.get(i)).setValue(new ArrayList<String>() {{ add(itemType.get(finalI));  }});
                }
                
                sbTypeList = cfg.getNode(bindTypes.get(i)).getList(TypeToken.of(String.class));

                if (cfg.getNode(mod,permCheck,enableType.get(i)).isVirtual())
                    cfg.getNode(mod,permCheck,enableType.get(i)).setValue(false);
                boolTypeList = cfg.getNode(mod,permCheck,enableType.get(i)).getBoolean();
            }

            getCfgMgr().save(cfg);
            getLogger().info(Console.SAVE_COMPLETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}