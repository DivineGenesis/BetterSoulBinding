package com.divinegenesis.soulbound.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.divinegenesis.soulbound.Reference;
import com.divinegenesis.soulbound.data.SoulboundKeys;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import static com.divinegenesis.soulbound.events.EventUtils.*;
import static com.divinegenesis.soulbound.Reference.getLore;

public class EventListener
{
	
	@Listener
    public void oPickup(ChangeInventoryEvent.Pickup.Pre event, @First Player player)
	{
        if(player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm) 
        {
        	ItemStack stack = event.getTargetEntity().item().get().createStack();
        	
        	if(stack.get(SoulboundKeys.SOULBOUND_USER).isPresent())
        	{
        		if(stack.get(Keys.ITEM_LORE).isPresent())
        		{
        			for(Text t : getLore(stack))
        			{
        				if(t.toPlain().equals("Bound to: none"))
        				{
        					loreAdd(new ArrayList<>(), player, stack);
        					break;
        				}
        			}
        		}
        		
        		UUID user = stack.get(SoulboundKeys.SOULBOUND_USER).get();
            	if(!user.toString().equals(player.getUniqueId().toString()))
            	{
            		errorMessage(player);
            		event.setCancelled(true);
            	}
        	}
        	else if(Reference.sb_pickup.contains(Reference.getID(stack)))
        	{
        		
        	}
        }
    }
	
//	@Listener
//    public void onPickup(ChangeInventoryEvent.Pickup.Pre event, @First Player player)
//	{
//        if (player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm) 
//        {
//            ItemStack stack = event.getTargetEntity().item().get().createStack();
//            List<Text> itemLore = new ArrayList<>();
//            String id = Reference.getID(stack);
//                if ((stack.get(Keys.ITEM_LORE).isPresent())) 
//                {
//                    for (Text t : getLore(stack)) 
//                    {
//                    	if(t.toPlain().equals("Bound to: none"))
//                    	{
//                    		loreAdd(itemLore, player, stack);
//                    		event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
//                    		break;
//                    	}
//                    	else if (t.toPlain().startsWith("UUID: ")) 
//                        {
//                            String UUID = t.toPlain().substring(6);
//                            if (!UUID.equals(player.getUniqueId().toString())) 
//                            {
//                                errorMessage(player);
//                                event.setCancelled(true);
//                            }
//                            break;
//                        }
//                        if (t.toPlain().equals(getLore(stack).get(getLore(stack).size() - 1).toPlain())) 
//                        {
//                            loreAdd(itemLore, player, stack);
//                            event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
//                        }
//                    }
//                }
//                else if (Reference.sb_pickup.contains(id)) 
//                {
//                    loreAdd(itemLore, player, stack);
//                    event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
//                }
//        }
//    }
	
/*
//Waiting implementation Not in API7

        @Listener
        public void onEquip(ChangeInventoryEvent.Equipment event, @First Player player)
        {

            player.sendMessage(Text.of("Equip event fired"));
            if(player.hasPermission(Reference.EQUIP) || !Reference.equip_perm)
            {

            }
        }
*/
	@Listener
	public void onHandUse(InteractItemEvent event, @Root Player player)
	{
		if((player.hasPermission(Reference.USE) || !Reference.use_perm))
		{
			char hand;
			
			if(event instanceof InteractItemEvent.Primary.MainHand || event instanceof InteractItemEvent.Secondary.MainHand)
				hand = 'm';
			else
				hand = 'o';
			
			ItemStack stack = event.getItemStack().createStack();
			event.setCancelled(handUse(player, stack, hand));		
		}
	}

   @Listener
   public void onDropItem(final DropItemEvent.Destruct event, @Root Player player) 
   {
	    if(player.hasPermission(Reference.KEEP_ON_DEATH) || !Reference.keep_perm) 
	    {

            final List<? extends Entity> soulboundItems = event.filterEntities(entity ->
                    !(entity instanceof Item) || !isSoulbound((Item) entity, player));

            soulboundItems.stream()
                    .map(Item.class::cast)
                    .map(Item::item)
                    .map(BaseValue::get)
                    .map(ItemStackSnapshot::createStack)
                    .forEach(itemStack -> player.getInventory().offer(itemStack)
                    );
        }
   }
}