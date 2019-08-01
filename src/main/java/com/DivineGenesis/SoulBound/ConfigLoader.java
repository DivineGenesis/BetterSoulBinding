package com.DivineGenesis.SoulBound;


import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;


public class ConfigLoader {

    private final Main plugin;

    private SBConfig SBConfig;
    private MessagesConfig messagesConfig;
    private static ConfigurationLoader loader;

    public ConfigLoader (Main main) {

        this.plugin = main;
        if (!plugin.getConfigDir().exists()) {
            plugin.getConfigDir().mkdirs();
        }
    }

    public boolean loadConfig () {

        try {
            File file = new File(plugin.getConfigDir(), "Soulbound.conf");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            loader = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode config = loader.load(ConfigurationOptions.defaults()
                                                           .setObjectMapperFactory(plugin.getFactory())
                                                           .setShouldCopyDefaults(true));
            SBConfig = config.getValue(TypeToken.of(SBConfig.class), new SBConfig());
            loader.save(config);
            return true;
        } catch (Exception e) {
            Main.getInstance().getLogger().error("Could not load config.", e);
            return false;
        }
    }

    public boolean loadMessages () {

        try {
            File file = new File(plugin.getConfigDir(), "Messages.conf");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            ConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode config = loader.load(ConfigurationOptions.defaults()
                                                           .setObjectMapperFactory(plugin.getFactory())
                                                           .setShouldCopyDefaults(true));
            messagesConfig = config.getValue(TypeToken.of(MessagesConfig.class), new MessagesConfig());
            loader.save(config);
            return true;
        } catch (Exception e) {
            Main.getInstance().getLogger().error("Could not load config.", e);
            return false;
        }
    }

    void saveConfig (SBConfig newConfig) {

        try {
            File file = new File(plugin.getConfigDir(), "Soulbound.conf");
            if (!file.exists()) {
                file.createNewFile();
            }
            ConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode configurationNode = loader.load(ConfigurationOptions.defaults()
                                                                      .setObjectMapperFactory(plugin.getFactory())
                                                                      .setShouldCopyDefaults(true));
            configurationNode.setValue(TypeToken.of(SBConfig.class), newConfig);
            loader.save(configurationNode);
        } catch (IOException | ObjectMappingException e) {
            plugin.getLogger().error("Could not save config", e);
        }
    }

    public SBConfig getSBConfig () {

        return SBConfig;
    }

    public MessagesConfig getMessagesConfig () {

        return messagesConfig;
    }
}