package xyz.divinegenesis.soulbound;


import java.util.UUID;


public class Reference {

    private static Reference reference;

    public Reference () {

        reference = this;
    }

    //plugin info
    public static final String ID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VERSION@";
    public static final String NUCLEUS_VERSION = "@NUCLEUS@";
    public static final String DESCRIPTION = "@DESCRIPTION@";

    //UUID String
    public final UUID Blank_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    //Permissions

    //users
    public final String PICKUP = "soulbound.user.pickup";
    public final String USE = "soulbound.user.use";
    public final String KEEP_ON_DEATH = "soulbound.user.keep";
    public final String CRAFT = "soulbound.user.craft";
    public final String PREVENT_CLEAR = "soulbound.user.despawn";
    public final String INVENTORY = "soulbound.user.inventory";
    public final String ENCHANT = "soulbound.user.enchant";

    //admins
    public final String HELP = "soulbound.admin.help";
    public final String ADD_LIST = "soulbound.admin.addlist";
    public final String REMOVE_LIST = "soulbound.admin.removelist";
    public final String ADD_SB = "soulbound.admin.addsb";
    public final String REMOVE_SB = "soulbound.admin.removesb";

    public final String BYPASS = "soulbound.admin.bypass";

    public static Reference getReference () {

        return reference;
    }
}
