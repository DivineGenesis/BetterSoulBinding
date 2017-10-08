package com.gmail.drzoddiak.soulbound.EventListeners;

import java.util.ArrayList;
import java.util.List;

import com.gmail.drzoddiak.soulbound.Reference;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import static com.gmail.drzoddiak.soulbound.EventListeners.EventUtils.*;
import static com.gmail.drzoddiak.soulbound.Reference.getLore;

/* **Imports for death harvest**
import java.util.Optional;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.HarvestEntityEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
*/

public class EventListener
{

	@Listener
    public void onPickup(ChangeInventoryEvent.Pickup event, @First Player player) {
        if (player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm) {
            ItemStack stack = event.getTargetEntity().item().get().createStack();
            List<Text> itemLore = new ArrayList<>();
            String id = Reference.getID(stack);
                if ((stack.get(Keys.ITEM_LORE).isPresent())) {
                    for (Text t : getLore(stack)) {
                        if (t.toPlain().startsWith("UUID: ")) {
                            String UUID = t.toPlain().substring(6);
                            if (!UUID.equals(player.getUniqueId().toString())) {
                                errorMessage(player);
                                event.setCancelled(true);
                            }
                            break;
                        }
                        if (t.toPlain().equals(getLore(stack).get(getLore(stack).size() - 1).toPlain())) {
                            loreAdd(itemLore, player, stack);
                            event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                        }
                    }
                }
                else if (Reference.sb_pickup.contains(id)) {
                    loreAdd(itemLore, player, stack);
                    event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                }
        }
    }

	/*Not Implemented Yet `6.0.0`
        @Listener
        public void onEquip(ChangeInventoryEvent.Equipment event, @First Player player)
        {

            player.sendMessage(Text.of("Equip event fired"));
            if(player.hasPermission(Reference.EQUIP) || !Reference.equip_perm)
            {

            }
        }*/

	@Listener
	public void onUse(InteractItemEvent.Primary.MainHand event, @First Player player)
	{
		if((player.hasPermission(Reference.USE) || !Reference.use_perm))
		{
			ItemStack stack = event.getItemStack().createStack();
			event.setCancelled(handUse(player, stack, 'm'));
		}

    }

	@Listener
    public void onOffHandUse(InteractItemEvent.Primary.OffHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
            event.setCancelled(handUse(player, stack, 'o'));
		}
    }

	@Listener
    public void onMainSecUse(InteractItemEvent.Secondary.MainHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
            event.setCancelled(handUse(player, stack, 'm'));
		}
    }

	@Listener
    public void onSecOffHandUse(InteractItemEvent.Secondary.OffHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
            event.setCancelled(handUse(player, stack, 'o'));
		}
    }
	
	/*
	@Listener
	public void onDeathHarvest(HarvestEntityEvent.TargetPlayer event, @First Player player)
	{
		//In theory SHOULD work once this event is fully implemented
		
		if(player.hasPermission(Reference.KEEP_ON_DEATH) || !Reference.keep_perm)
		{
			Iterable<Inventory> playerInv = event.getTargetEntity().getInventory().query(GridInventory.class).slots();
			
			event.setKeepsInventory(true);
			event.setCancelled(true);
			
			//Non-Soulbound removal
			for(Inventory i: playerInv)
			{
				Slot slot = (Slot)i;
				Optional<ItemStack> stack = slot.peek();
				String uuid = "";
				
				if(stack.isPresent())
				{
					List<Text> lore = stack.get().get(Keys.ITEM_LORE).get();
					for(int k=0; k<lore.size();k++)
					{
						if(lore.get(k).toPlain().startsWith("UUID:"))
						{
							uuid = lore.get(k).toPlain().substring(6);
							break;
						}
					}
					
					if(!player.getUniqueId().equals(uuid))
					{
						slot.clear(); //empties slot
						//spawn
						Entity itemEntity = event.getTargetEntity().getWorld().
								createEntity(EntityTypes.ITEM, event.getTargetEntity().getLocation().getPosition());
						Item items = (Item) itemEntity;
						items.offer(Keys.REPRESENTED_ITEM, stack.get().createSnapshot());
						event.getTargetEntity().getWorld().spawnEntity(items, Cause.of(NamedCause.simulated(event.getTargetEntity())));
					}
				}
			}
		}
	}
	*/
	// Done
}