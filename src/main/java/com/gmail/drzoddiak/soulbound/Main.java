package com.gmail.drzoddiak.soulbound;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import org.apache.commons.lang3.ArrayUtils;
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
import java.util.List;

import static com.gmail.drzoddiak.soulbound.Reference.*;
@Plugin(
        name = NAME,
        id = ID,
        version = VERSION,
        description = DESC,
        authors = AUTHORS
)
public class Main {
    public Main instance;
    private Logger logger; 
    private ConfigurationNode cfg;

    private File defaultCfg;

    private ConfigurationLoader<CommentedConfigurationNode> cfgMgr;

    private Game game;

    private void registerEvents() {
        Sponge.getEventManager().registerListeners(this, new EventListener());
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        getLogger().info("Starting plugin...");
        registerEvents();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        this.logger.info("Hello world!");
    }

        @Inject
        public Main(Logger logger, Game game, @DefaultConfig(sharedRoot = false) File defaultCfg, @DefaultConfig
                (sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> cfgMgr) {
            this.logger = logger;
            this.game = game;
            this.defaultCfg = defaultCfg;
            this.cfgMgr = cfgMgr;
            instance = this;
        }

        @Listener
        public void onPreInit(GamePreInitializationEvent event) {
            getLogger().info("Making config...");
            try {
                if (!defaultCfg.exists()) {
                    defaultCfg.createNewFile();

                    this.cfg = getCfgMgr().load();
                    this.cfg.getNode("Bind Upon Use").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_SWORD.getId());}});
                    this.cfg.getNode("Bind Upon Pickup").setValue(new ArrayList<String>(){{add(ItemTypes.COAL.getId());}});
                    this.cfg.getNode("Bind Upon Equip").setValue(new ArrayList<String>(){{add(ItemTypes.DIAMOND_HELMET.getId());}});
                    getLogger().info("Config created.");
                    getCfgMgr().save(cfg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try 
            {
				sb_use = this.cfg.getNode("Bind Upon Use").getList(TypeToken.of(String.class));
	        	sb_pickup = this.cfg.getNode("Bind Upon Pickup").getList(TypeToken.of(String.class));
	           	sb_equip = this.cfg.getNode("Bind Upon Equip").getList(TypeToken.of(String.class));
			}
            catch (ObjectMappingException e)
            {
				e.printStackTrace();
			}
        }

        public ConfigurationLoader<CommentedConfigurationNode> getCfgMgr() {
            return cfgMgr;
        }

        public Logger getLogger() {
            return logger;
        }

        public ConfigurationNode getCfg() {
            return cfg;
        }

        public Game getGame() {
            return game;
        }
}
