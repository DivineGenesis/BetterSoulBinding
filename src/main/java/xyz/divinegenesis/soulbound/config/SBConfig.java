package xyz.divinegenesis.soulbound.config;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.List;


@ConfigSerializable
public class SBConfig {

    @Setting (comment = "Setting a module as true means that it will check the player for permissions. If the module is set as false it will check everyone regardless of permissions set")
    public Modules modules = new Modules();

    @Setting (comment = "This is only used if you use Nucleus, otherwise you may ignore it.")
    public Nucleus nucleus = new Nucleus();

    @Setting
    public USE use = new USE();

    @Setting
    public PICKUP pickup = new PICKUP();

    @Setting
    public CRAFT craft = new CRAFT();

    @Setting
    public ENCHANT enchant = new ENCHANT();

    @Setting
    public TRANSACTION transaction = new TRANSACTION();

    @ConfigSerializable
    public static class ENCHANT {

        @Setting (value = "Bind Upon Enchant", comment = "IDs in this list are bound when an item is \"Enchanted\" with an anvil Should be formatted as such: mod:id")
        public List<String> BindOnEnchant = new ArrayList<String>() {{
            add(ItemTypes.KNOWLEDGE_BOOK.getId());
        }};

        @Setting (value = "Item for Binding", comment = "Item here is used to bind the item in an anvil")
        public String BindingItem = ItemTypes.EGG.getId();

        @Setting (value = "Check permission to enchant an item with soulbound")
        public boolean enchantPermissions = false;
    }

    @ConfigSerializable
    public static class CRAFT {

        @Setting (value = "Bind Upon Craft", comment = "IDs in this list are bound when an item is crafted Should be formatted as such: mod:id")
        public List<String> BindOnCraft = new ArrayList<String>() {{
            add(ItemTypes.DIAMOND_HELMET.getId());
        }};

        @Setting (value = "Check permission for Craft")
        public boolean CraftPermissions = false;
    }

    @ConfigSerializable
    public static class PICKUP {

        @Setting (value = "Bind Upon Pickup", comment = "IDs in this list are bound when a user picks an item up Should be formatted as such: mod:id")
        public List<String> BindOnPickup = new ArrayList<String>() {{
            add(ItemTypes.COAL.getId());
        }};

        @Setting (value = "Check permission for Pickup")
        public boolean PickupPermissions = false;
    }

    @ConfigSerializable
    public static class USE {

        @Setting (value = "Bind Upon Use", comment = "IDs in this list are bound when an item is used. Should be formatted as such: mod:id")
        public List<String> BindOnUse = new ArrayList<String>() {{
            add(ItemTypes.DIAMOND_SWORD.getId());
        }};

        @Setting (value = "Check permission for Use")
        public boolean UsePermissions = false;
    }

    @ConfigSerializable
    public static class TRANSACTION {

        @Setting (value = "Bind Upon Inventory Transaction", comment = "IDs in this list are bound when an item is taken from an inventory, " + "such as a chest. Should be formatted as such: mod:id")
        public List<String> BindOnInventoryTransaction = new ArrayList<String>() {{
            add(ItemTypes.BONE.getId());
        }};

        @Setting (value = "Check permission for Inventory transactions")
        public boolean TransactionPermissions = false;
    }


    @ConfigSerializable
    public static class Modules {

        @Setting (value = "Check permission for preventing soulbound items from being naturally cleared")
        public boolean preventClearPermissions = false;

        @Setting (value = "Check permission for keeping soulbound items after death")
        public boolean KeepItemsOnDeath = false;

        @Setting (value = "DEBUG", comment = "For development purposes, do not enable this unless you like spam")
        public boolean DebugInfo = false;
    }

    @ConfigSerializable
    public static class Nucleus {

        @Setting (value = "Bind all items received from kits (Ignores list)")
        public boolean BindKitItems = false;

        @Setting (value = "Bind Upon Kit Redemption", comment = "IDs in this list are bound when an item is received via kit.")
        public List<String> BindOnRedeem = new ArrayList<String>() {{
            add(ItemTypes.APPLE.getId());
        }};
    }
}
