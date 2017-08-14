package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.HarvestEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EventListener
{

	@Listener
    public void onPickup(ChangeInventoryEvent.Pickup event, @First Player player)
	{
		if(player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm)
		{
			ItemStack stack = event.getTargetEntity().item().get().createStack();
			String id = Reference.getID(stack.getItem().getTemplate().createStack());
			List<Text> itemLore = new ArrayList<>();
			
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

	//Left Click - main hand & Done

	@Listener
	public void onUse(InteractItemEvent.Primary.MainHand event, @First Player player)
	{


		if((player.hasPermission(Reference.USE) || !Reference.use_perm))
		{
			ItemStack stack = event.getItemStack().createStack();
			String id = Reference.getID(event.getItemStack().createStack());
			List<Text> itemLore = new ArrayList<>();
			if (stack.get(Keys.ITEM_LORE).isPresent())
			{

				for(int i=0; i<stack.get(Keys.ITEM_LORE).get().size();i++)
				{
					if(stack.get(Keys.ITEM_LORE).get().get(i).toPlain().startsWith("UUID: "))
						{
							String UUID = stack.get(Keys.ITEM_LORE).get().get(i).toPlain().substring(6);
							if(!(UUID.equals(player.getUniqueId().toString())))
								{
									event.setCancelled(true);
									errorMessage(player);
								}

								break;
						}
				}

			}
            else if (Reference.sb_use.contains(id))
            {
                itemLore.add(Text.of("Bound to: "+player.getName()));
                itemLore.add(Text.of("UUID: "+player.getUniqueId()));
                stack.offer(Keys.ITEM_LORE, itemLore);
                player.setItemInHand(HandTypes.MAIN_HAND, stack);
            }

=======

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
    public void onPickup(ChangeInventoryEvent.Pickup event, @First Player player)
	{
		if(player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm)
		{

			ItemStack stack = event.getTargetEntity().item().get().createStack();
			String id = Reference.getID(stack.getItem().getTemplate().createStack());
			List<Text> itemLore = new ArrayList<>();
			
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

	//Left Click - main hand & Done
	@Listener
	public void onUse(InteractItemEvent.Primary.MainHand event, @First Player player)
	{
		if((player.hasPermission(Reference.USE) || !Reference.use_perm))
		{
			ItemStack stack = event.getItemStack().createStack();
			handUse(player, stack, 'm');
>>>>>>> refs/remotes/origin/Test-branch
		}
    }

	//Left Click - offhand & Done
	@Listener
    public void onOffHandUse(InteractItemEvent.Primary.OffHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
<<<<<<< HEAD
			String id = Reference.getID(event.getItemStack().createStack());
			List<Text> itemLore = new ArrayList<>();
			if (stack.get(Keys.ITEM_LORE).isPresent())
			{

				for(int i=0; i<stack.get(Keys.ITEM_LORE).get().size();i++)
				{
					if(stack.get(Keys.ITEM_LORE).get().get(i).toPlain().startsWith("UUID: "))
					{
						String UUID = stack.get(Keys.ITEM_LORE).get().get(i).toPlain().substring(6);
						if(!(UUID.equals(player.getUniqueId().toString())))
						{
							event.setCancelled(true);
							errorMessage(player);
						}
						break;
					}
				}

			}
            else if (Reference.sb_use.contains(id))
            {
                itemLore.add(Text.of("Bound to: "+player.getName()));
                itemLore.add(Text.of("UUID: "+player.getUniqueId()));
                stack.offer(Keys.ITEM_LORE, itemLore);
                player.setItemInHand(HandTypes.OFF_HAND, stack);
            }
=======
            handUse(player, stack, 'o');
		}
    }
	
	//Right Click - main hand & Done
	@Listener
    public void onMainSecUse(InteractItemEvent.Secondary.MainHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
            handUse(player, stack, 'm');
>>>>>>> refs/remotes/origin/Test-branch
		}
    }
	
	//Right Click - main hand & Done
	@Listener
    public void onMainSecUse(InteractItemEvent.Secondary.MainHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
			String id = Reference.getID(event.getItemStack().createStack());
			List<Text> itemLore = new ArrayList<Text>();
			if (stack.get(Keys.ITEM_LORE).isPresent()) {

				for (int i = 0; i < stack.get(Keys.ITEM_LORE).get().size(); i++) {
					if (stack.get(Keys.ITEM_LORE).get().get(i).toPlain().startsWith("UUID: ")) {
						String UUID = stack.get(Keys.ITEM_LORE).get().get(i).toPlain().substring(6);
						if (!(UUID.equals(player.getUniqueId().toString()))) {
                            event.setCancelled(true);
							errorMessage(player);
						}
						break;
					}

<<<<<<< HEAD
				}

			}
            else if (Reference.sb_use.contains(id))
            {
                itemLore.add(Text.of("Bound to: "+player.getName()));
                itemLore.add(Text.of("UUID: "+player.getUniqueId()));
                stack.offer(Keys.ITEM_LORE, itemLore);
                player.setItemInHand(HandTypes.MAIN_HAND, stack);
            }
=======
	//Right Click - offhand & Done
	@Listener
    public void onSecOffHandUse(InteractItemEvent.Secondary.OffHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
            handUse(player, stack, 'o');
>>>>>>> refs/remotes/origin/Test-branch
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

<<<<<<< HEAD
	//Right Click - offhand & Done
	@Listener
    public void onSecOffHandUse(InteractItemEvent.Secondary.OffHand event, @First Player player)
	{
		if(player.hasPermission(Reference.USE) || !Reference.use_perm)
		{
			ItemStack stack = event.getItemStack().createStack();
			String id = event.getItemStack().getType().getId();
			List<Text> itemLore = new ArrayList<Text>();
			if (stack.get(Keys.ITEM_LORE).isPresent()) {

				for (int i = 0; i < stack.get(Keys.ITEM_LORE).get().size(); i++) {
					if (stack.get(Keys.ITEM_LORE).get().get(i).toPlain().startsWith("UUID: ")) {
						String UUID = stack.get(Keys.ITEM_LORE).get().get(i).toPlain().substring(6);
						if (!(UUID.equals(player.getUniqueId().toString()))) {
							event.setCancelled(true);
							errorMessage(player);
						}
						break;
					}

				}
			}
            else if (Reference.sb_use.contains(id))
            {
                itemLore.add(Text.of("Bound to: "+player.getName()));
                itemLore.add(Text.of("UUID: "+player.getUniqueId()));
                stack.offer(Keys.ITEM_LORE, itemLore);
                player.setItemInHand(HandTypes.OFF_HAND, stack);
            }
		}
    }
	
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
					System.out.println(stack.get().getItem().getId());
					for(int k=0; k<lore.size();k++)
					{
						if(lore.get(k).toPlain().startsWith("UUID:"))
						{
							uuid = lore.get(k).toPlain().substring(6);
							System.out.println(uuid);
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
	// Done
	private void errorMessage(Player player)
	{
		player.sendMessage(Text.of(TextColors.DARK_RED, "This Item is not Soulbound to you!"));
=======
	private void handUse(Player player, ItemStack stack, char hand)
	{
		String id = Reference.getID(stack);
		List<Text> itemLore = new ArrayList<Text>();
		
		if(stack.get(Keys.ITEM_LORE).isPresent())
		{
			for(Text t: getLore(stack))
			{
				if(t.toPlain().startsWith("UUID: "))
				{
					String UUID = t.toPlain().substring(6);
					if(!UUID.equals(player.getUniqueId().toString()))
					{
						errorMessage(player);
					}
					break;
				}
				if(t.toPlain().equals( getLore(stack).get(getLore(stack).size()-1) ))
				{
					itemLore = getLore(stack);
					loreAdd(itemLore, player, stack, hand);
				}
			}
		}
		else if(Reference.sb_use.contains(id))
		{
			loreAdd(itemLore, player, stack, hand);
		}
	}
    
    private void loreAdd(List<Text> itemLore, Player player, ItemStack stack, char hand)
    {
    	itemLore.add(Text.of("Bound to: "+player.getName()));
    	itemLore.add(Text.of("UUID: "+player.getUniqueId().toString()));
        stack.offer(Keys.ITEM_LORE, itemLore);
        
        switch(hand)
        {
        	case 'm':
        		player.setItemInHand(HandTypes.MAIN_HAND, stack);
        		break;
        	case 'o':
        		player.setItemInHand(HandTypes.OFF_HAND, stack);
        		break;
        	default:
        		player.sendMessage(Text.of(TextColors.DARK_RED, "ERROR HAS OCCURRED, OH NOES!"));
        		break;
        }
    }
    
	private void errorMessage(Player player)
	{
		player.sendMessage(Text.of(TextColors.DARK_RED, "This item is not soulbound to you!"));
>>>>>>> refs/remotes/origin/Test-branch
	}
}