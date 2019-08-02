package com.DivineGenesis.SoulBound.config;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;


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
        public String HELP_MENU_ADD_CONFIG = "Adds items to config";

        @Setting
        public String HELP_MENU_REMOVE_CONFIG = "Removes items fron the config";

        @Setting
        public String HELP_MENU_ADD_SOULBOUND = "Adds soulbound trait to an item";

        @Setting
        public String HELP_MENU_REMOVE_SOULBOUND = "Removes the soulbound trait from an item";
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
    }


}
