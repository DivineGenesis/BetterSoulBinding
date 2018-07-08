package com.DivineGenesis.SoulBound;

public class Messages 
{
	public static class Player
	{
		public static final String 
			NOT_YOURS = "This item is not soulbound to you!",
			ERROR = "ERROR HAS OCCURRED";
	}
	
	public static class Console
	{
		//config msgs
		public static final String 
				NO_CONFIG= "No configuration file yet? Don't worry, we got that covered! \n Creating config...", 
				CONFIG_CREATED = "Config created.", 
				SAVING_VARIABLES = "Saving config data into variables!",  
				SAVE_COMPLETE = "Yay! Data was saved :D",
				RELOAD = "Reloading...\nChecking config...",
				LOADED = "Soulbinding has loaded",
				REGISTERING = "Registering Events & Commands...";
	}
	
	public static class Config
	{
		//Node Names
		public static final String 
			useEn = "Use-Enabled",  
			pickupEn = "Pickup-Enabled", 
			keepEn = "KeepUponDeath-Enabled",
			mod = "Modules", 
			permCheck = "Permission-Check", 
			craftEn = "Craft-Enabled",
			pickup = "Bind Upon Pickup",
			use = "Bind Upon Use",
		    craft = "Bind Upon Craft";
	}
}