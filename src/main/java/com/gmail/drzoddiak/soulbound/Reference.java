package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;
 

public class Reference 
{
//@Plugin data parameters
	public static final String NAME = "Main";
	public static final String ID = "soulbound";
	public static final String VERSION = "0.0.1";
	public static final String DESC = "Binds items to the users blood!";
	public static final String AUTHORS = "DrZoddiak & Burpingdog1";

//Permissions
	//users
	public static final String PICKUP = "soulbound.users.pickup";
	public static final String USE = "soulbound.users.use";
	public static final String EQUIP = "soulbound.users.equip";
	public static final String KEEP_ON_DEATH = "soulbound.users.keep";
	//admins
	public static final String HELP = "soulbound.admin";
	public static final String ADD_LIST = "soulbound.admin.addlist";
	public static final String REMOVE_LIST = "soulbound.admin.removelist";
	public static final String ADD_SB = "soulbound.admin.addsb";
	public static final String REMOVE_SB = "soulbound.admin.removesb";
	
	//Plugin config data
	public static List<String> sb_pickup = new ArrayList<String>();
	public static List<String> sb_use = new ArrayList<String>(); 
	public static List<String> sb_equip = new ArrayList<String>(); 
	public static boolean pickup_perm;
	public static boolean use_perm;
	public static boolean equip_perm;
	public static boolean keep_perm;
	
	public static boolean addPickup(String id)
	{
		if(sb_pickup.contains(id))
			return false;
		sb_pickup.add(id);
		Main.saveFile();
		return true;
	}
	
	public static boolean addUse(String id)
	{
		if (sb_use.contains(id)) 
			return false;
		sb_use.add(id);
		Main.saveFile();
		return true;
	}
	
	public static boolean addEquip(String id)
	{
		if(sb_equip.contains(id))
			return false;
		sb_equip.add(id);
		Main.saveFile();
		return true;
	}
	
	public static boolean removePickup(String id)
	{
		if(!sb_pickup.contains(id))
		{
			sb_pickup.remove(id);
			Main.saveFile();
			return true;
		}
		return false;
	}
	
	public static boolean removeUse(String id)
	{
		if(!sb_use.contains(id))
		{
			sb_use.remove(id);
			Main.saveFile();
			return true;
		}
		return false;
	}
	
	public static boolean removeEquip(String id)
	{
		if(!sb_equip.contains(id))
		{
			sb_equip.remove(id);
			Main.saveFile();
			return true;
		}
		return false;
	}
}