package com.gmail.drzoddiak.soulbound;

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
	static final String NAME = "Main";
	static final String ID = "soulbound";
	static final String VERSION = "0.6.48";
	static final String DESC = "Binds items to the users soul!";
	static final String AUTHORS = "DrZoddiak & Burpingdog1";

//Permissions
	//users
	 static final String PICKUP = "soulbound.user.pickup";
	 static final String USE = "soulbound.user.use";
	 static final String EQUIP = "soulbound.user.equip";
	 //static final String KEEP_ON_DEATH = "soulbound.user.keep";
	 //admins
	 static final String HELP = "soulbound.admin.help";
	 static final String ADD_LIST = "soulbound.admin.addlist";
	 static final String REMOVE_LIST = "soulbound.admin.removelist";
	 static final String ADD_SB = "soulbound.admin.addsb";
	 static final String REMOVE_SB = "soulbound.admin.removesb";

	 //Plugin config data
	 static List<String> sb_pickup = new ArrayList<>();
	 static List<String> sb_use = new ArrayList<>();
	 static List<String> sb_equip = new ArrayList<>();
	 static boolean pickup_perm;
	 static boolean use_perm;
	 static boolean equip_perm;
	 //static boolean keep_perm;

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
		case 2: //equip
			if(sb_equip.contains(id))
				return false;
			sb_equip.add(id);
			SBConfig.saveToFile();
			return true;
		}
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
		case 2://equip
			if(sb_equip.contains(id))
			{
				sb_equip.remove(id);
				SBConfig.saveToFile();
				return true;
			}
			return false;
		}
		return false;
	}

    static List<Text> getLore(ItemStack stack)
    {
        return stack.get(Keys.ITEM_LORE).get();
    }

	static String getID(ItemStack stack)
	{
		String ID = stack.getItem().getId();
		DataContainer container = stack.toContainer();
		DataQuery data = DataQuery.of('/', "UnsafeDamage");
		int meta = Integer.parseInt(container.get(data).get().toString());
		if(meta != 0 && stack.getValue(Keys.UNBREAKABLE).get().get())
		{
			ID = ID+":"+meta;
		}
		return ID;
	}
}