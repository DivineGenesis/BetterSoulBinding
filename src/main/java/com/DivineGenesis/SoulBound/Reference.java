package com.DivineGenesis.SoulBound;


import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.UUID;


public class Reference {
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
    public static final String KEEP_ON_DEATH = "soulbound.user.keep";
    public static final String CRAFT = "soulbound.user.craft";

    //admins
    static final String HELP = "soulbound.admin.help";
    static final String ADD_LIST = "soulbound.admin.addlist";
    static final String REMOVE_LIST = "soulbound.admin.removelist";
    static final String ADD_SB = "soulbound.admin.addsb";
    static final String REMOVE_SB = "soulbound.admin.removesb";

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
