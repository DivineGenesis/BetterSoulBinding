package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class EventListener 
{

	@Listener
    public void onPickup(ChangeInventoryEvent.Pickup event, @First Player player)
	{
		if(player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm)
		{
			ItemStack stack = event.getTargetEntity().item().get().createStack();
			String id = stack.getItem().getId();
			List<Text> itemLore = new ArrayList<Text>();
			
			if(Reference.sb_pickup.contains(id))
			{
				//WIP
			}
		}
	}

	@Listener
    public void onEquip(ChangeInventoryEvent.Equipment event, @First Player player)
	{
		if(player.hasPermission(Reference.EQUIP) || !Reference.equip_perm)
		{
			//WIP
		}
	}

	@Listener
    public void onUse(InteractItemEvent.Primary.MainHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
			String id = event.getItemStack().getType().getId();
			List<Text> itemLore = new ArrayList<Text>();
		
			if(Reference.sb_use.contains(id));
			{
				itemLore.add(Text.of("Soulbounded"));
				stack.offer(Keys.ITEM_LORE, itemLore);
				player.setItemInHand(HandTypes.MAIN_HAND, stack);
			}
		}
    }

	@Listener
    public void onSecUse(InteractItemEvent.Primary.OffHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
			String id = event.getItemStack().getType().getId();
			List<Text> itemLore = new ArrayList<Text>();
			
			if(Reference.sb_use.contains(id))
			{
				itemLore.add(Text.of("Soulbounded"));
				stack.offer(Keys.ITEM_LORE, itemLore);
				player.setItemInHand(HandTypes.OFF_HAND, stack);
			}
		}
    }
}