package xyz.divinegenesis.soulbound.config;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;


@ConfigSerializable
public class MessagesConfig {

    public Items items = new Items();

    public Help_Menu help_menu = new Help_Menu();

    @Setting
    public String PLUGIN_ERROR = "AN ERROR HAS OCCURRED. PLEASE REPORT TO AN ADMIN!";

    @Setting
    public String BIND_MESSAGE = "Bound to: ";

    @Setting
    public String COMMAND_MUST_BE_RUN_BY_PLAYER = "This command must be run by a player";


    @ConfigSerializable
    public static class Help_Menu {

        @Setting
        public String HELP_MENU_ADD_CONFIG = "modifies entries in config";

        @Setting
        public String HELP_MENU_REMOVE_SOULBOUND = "Adds or removes soulbound trait from an item";

        @Setting
        public Map<String,String> MAP = new HashMap<>();
    }

    @ConfigSerializable
    public static class Items {

        @Setting
        public String ITEM_NOT_BOUND_TO_PLAYER = "This item is not bound to you!";

        @Setting
        public String ITEM_NOT_PRESENT = "Item must be present in your hand to unbind!";

        @Setting
        public String ITEM_NOT_BOUND = "Item was not bound!";

        @Setting
        public String ITEM_ALREADY_BOUND = "This item has already been bound!";

        @Setting
        public String ITEM_SUCCESSFULLY_BOUND = "This item has been successfully bound!";

        @Setting
        public String ITEM_SUCCESSFULLY_REMOVED_BIND = "This item has been successfully removed from being soulbound!";

        @Setting
        public String ADMIN_BYPASS = "You were able to use/receive this item due to a permission bypass";
    }
}
