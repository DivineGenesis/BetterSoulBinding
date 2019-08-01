package com.DivineGenesis.SoulBound;


import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Reference {

    private static final Main plugin = Main.getInstance();

    //@Plugin data
    static final String NAME = "Soulbound";
    static final String ID = "soulbound";

    static final String VERSION = "2.0.0";

    static final String DESC = "Binds items to the users soul!";
    static final String AUTHORS = "DrZoddiak & Burpingdog1";

    //UUID String
    public static final UUID Blank_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    //Permissions

    //users
    public static final String PICKUP = "soulbound.user.pickup";
    public static final String USE = "soulbound.user.use";
    //public static final String EQUIP = "SoulBound.user.equip";
    public static final String KEEP_ON_DEATH = "soulbound.user.keep";
    public static final String CRAFT = "soulbound.user.craft";

    //admins
    static final String HELP = "soulbound.admin.help";
    static final String ADD_LIST = "soulbound.admin.addlist";
    static final String REMOVE_LIST = "soulbound.admin.removelist";
    static final String ADD_SB = "soulbound.admin.addsb";
    static final String REMOVE_SB = "soulbound.admin.removesb";

    //Plugin config data
    //public static List<String> sb_equip = new ArrayList<>();

    public static List<String> sb_craft = new ArrayList<>();
    public static boolean pickup_perm;
    public static boolean use_perm;
    //public static boolean equip_perm;
    public static boolean craft_perm;
    public static boolean keep_perm;

    static boolean addToList (String id, int index) {

        SBConfig config = plugin.getSBConfig();

        switch (index) {
            case 0: //pick up
                if (config.BindOnPickup.contains(id)) {
                    return false;
                }
                config.BindOnPickup.add(id);
                return true;

            case 1: //use
                if (config.BindOnUse.contains(id)) {
                    return false;
                }
                config.BindOnUse.add(id);
                return true;

            case 2: //Craft
                if (config.BindOnCraft.contains(id)) {
                    return false;
                }
                config.BindOnCraft.add(id);
                return true;
		/*case 3: //equip
			if(sb_equip.contains(id))
				return false;
			sb_equip.add(id);
			config.saveToFile();
			return true;*/
        }
        return false;
    }

    static boolean removeFromList (String id, int index) {

        SBConfig config = plugin.getSBConfig();

        switch (index) {
            case 0: //pickup
                if (config.BindOnPickup.contains(id)) {
                    config.BindOnPickup.remove(id);
                    return true;
                }
                return false;
            case 1://use
                if (config.BindOnUse.contains(id)) {
                    config.BindOnUse.remove(id);
                    return true;
                }
                return false;

            case 2: //Craft
                if (config.BindOnCraft.contains(id)) {
                    config.BindOnCraft.remove(id);
                    return true;
                }

        }
        return false;
    }

    public static List<Text> getLore (ItemStack stack) {

        return stack.get(Keys.ITEM_LORE).get();
    }

    public static String getID (ItemStack stack) {

        String ID = stack.getType().getId();
        DataContainer container = stack.toContainer();
        DataQuery data = DataQuery.of('/', "UnsafeDamage");
        int meta = Integer.parseInt(container.get(data).get().toString());
        if (meta != 0 && stack.getValue(Keys.UNBREAKABLE).isPresent()) {
            if (stack.getValue(Keys.UNBREAKABLE).get().get()) {
                ID += ":" + meta;
            }
        }
        return ID;
    }
}
