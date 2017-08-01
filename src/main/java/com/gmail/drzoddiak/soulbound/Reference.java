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
	public static final String PICKUP = "soulbound.pickup";
	public static final String USE = "soulbound.use";
	public static final String EQUIP = "soulbound.equip";
	public static final String KEEP_ON_DEATH = "soulbound.keep";
	
	
	//Plugin config data
	public static List<String> sb_pickup = new ArrayList<String>();
	public static List<String> sb_use = new ArrayList<String>(); 
	public static List<String> sb_equip = new ArrayList<String>(); 
	public static boolean pickup_perm;
	public static boolean use_perm;
	public static boolean equip_perm;
	public static boolean keep_perm;
	
}