package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;

import ninja.leaping.configurate.ConfigurationNode;
 

public class Reference 
{
	public static final String NAME = "Main";
	public static final String ID = "soulbound";
	public static final String VERSION = "0.0.1";
	public static final String DESC = "Binds items to the users blood!";
	public static final String AUTHORS = "DrZoddiak & Burpingdog1";
	
	public static List<String> sb_pickup = new ArrayList<String>();
	public static List<String> sb_use = new ArrayList<String>(); 
	public static List<String> sb_equip = new ArrayList<String>(); 
	
	public static void setLists(List<String> p, List<String> u, List<String> e)
	{
		sb_pickup = p;
		sb_use = u;
		sb_equip = e;
	}
}