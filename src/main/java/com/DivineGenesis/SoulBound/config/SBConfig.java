package com.DivineGenesis.SoulBound.config;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.List;


@ConfigSerializable
public class SBConfig {

    @Setting (value = "Bind Upon Use", comment = "IDs in this list are bound when an item is used. Should be formatted as such: mod:id")
    public List<String> BindOnUse = new ArrayList<String>() {{
        add(ItemTypes.DIAMOND_SWORD.getId());
    }};

    @Setting (value = "Bind Upon Pickup", comment = "IDs in this list are bound when a user picks an item up Should be formatted as such: mod:id")
    public List<String> BindOnPickup = new ArrayList<String>() {{
        add(ItemTypes.COAL.getId());
    }};

    @Setting (value = "Bind Upon Craft", comment = "IDs in this list are bound when an item is crafted Should be formatted as such: mod:id")
    public List<String> BindOnCraft = new ArrayList<String>() {{
        add(ItemTypes.DIAMOND_HELMET.getId());
    }};

    @Setting (comment = "Setting a module as true means that it will check the player for permissions. If the module is set as false it will " + "check everyone regardless of permissions set")
    public Modules modules = new Modules();

    @Setting (comment = "This is only used if you use Nucleus, otherwise you can ignore it.")
    public Nucleus nucleus = new Nucleus();

    @ConfigSerializable
    public static class Modules {

        @Setting (value = "Apply permission on for Use", comment = "Default value: false")
        public boolean UsePermissions = false;

        @Setting (value = "Apply permission on for Pickup", comment = "Default value: false")
        public boolean PickupPermissions = false;

        @Setting (value = "Apply permission on for Craft", comment = "Default value: false")
        public boolean CraftPermissions = false;

        @Setting (value = "Apply permission on for keeping soulbound items after death", comment = "Default value: false")
        public boolean KeepItemsOnDeath = false;
    }

    @ConfigSerializable
    public static class Nucleus {

        @Setting (value = "Bind all items recieved from kits (Ignores list)")
        public boolean BindKitItems = false;

        @Setting (value = "Bind Upon Kit Redemption", comment = "IDs in this list are bound when an item is recieved via kit.")
        public List<String> BindOnRedeem = new ArrayList<String>() {{
            add(ItemTypes.APPLE.getId());
        }};

    }

}
