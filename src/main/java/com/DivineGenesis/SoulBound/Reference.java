
package com.DivineGenesis.SoulBound;


import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;


public class Reference
{
//@Plugin data parameters
	static final String NAME = "Soulbound";
	static final String ID = "soulbound";

	static final String VERSION = "0.10.3";

	static final String DESC = "Binds items to the users soul!";
	static final String AUTHORS = "DrZoddiak & Burpingdog1";

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

	 //Plugin config data
	 public static List<String> sb_pickup = new ArrayList<>();
	 public static List<String> sb_use = new ArrayList<>();
	 //public static List<String> sb_equip = new ArrayList<>();

	 public static List<String> sb_craft = new ArrayList<>();
	 public static boolean pickup_perm;
	 public static boolean use_perm;
	 //public static boolean equip_perm;
	 public static boolean craft_perm;

	 public static boolean keep_perm;

    static boolean addToList(String id, int index)
	{
		switch (index)
		{
		case 0: //pick up
			if(sb_pickup.contains(id))
				return false;
			sb_pickup.add(id);
			SBConfig.saveToFile();
			return true;

			case 1: //use
				if (sb_use.contains(id))
					return false;
				sb_use.add(id);
				SBConfig.saveToFile();
			return true;

			case 2: //Craft
				if(sb_craft.contains(id))
					return false;
				sb_craft.add(id);
				SBConfig.saveToFile();
				return true;
			}
		/*case 3: //equip

			if(sb_equip.contains(id))
				return false;
			sb_equip.add(id);
			SBConfig.saveToFile();
			return true;
		}*/
		return false;
	}
	
	static boolean removeFromList(String id, int index)
	{
		switch (index)
		{
		case 0: //pickup
			if(sb_pickup.contains(id))
			{
				sb_pickup.remove(id);
				SBConfig.saveToFile();
				return true;
			}
			return false;
		case 1://use
			if(sb_use.contains(id))
			{
				sb_use.remove(id);
				SBConfig.saveToFile();
				return true;
			}
			return false;

			case 2: //Craft
				if(sb_craft.contains(id))
				{
					sb_craft.remove(id);
					SBConfig.saveToFile();
					return true;
				}
		/*case 3://equip

			if(sb_equip.contains(id))
			{
				sb_equip.remove(id);
				SBConfig.saveToFile();
				return true;
			}
			return false;*/
		}
		return false;
	}

    public static List<Text> getLore(ItemStack stack)
    {
        return stack.get(Keys.ITEM_LORE).get();
    }

	public static String getID(ItemStack stack)
	{
		String ID = stack.getType().getId();
		DataContainer container = stack.toContainer();
		DataQuery data = DataQuery.of('/', "UnsafeDamage");
		int meta = Integer.parseInt(container.get(data).get().toString());
		if(meta != 0 && stack.getValue(Keys.UNBREAKABLE).isPresent()){
			if(stack.getValue(Keys.UNBREAKABLE).get().get())
			{
				ID += ":"+meta;
			}
		}
		return ID;
	}
}
