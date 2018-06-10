package com.DivineGenesis.SoulBound.EventListeners;

import java.util.ArrayList;
import java.util.List;

import com.DivineGenesis.SoulBound.Reference;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.*;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import static com.DivineGenesis.SoulBound.EventListeners.EventUtils.*;
import static com.DivineGenesis.SoulBound.Reference.getLore;
import static com.DivineGenesis.SoulBound.Reference.getID;

public class EventListener
{
	@Listener
    public void onPickup(ChangeInventoryEvent.Pickup.Pre event, @First Player player)
	{
        if (player.hasPermission(Reference.PICKUP) || !Reference.pickup_perm) 
        {
            ItemStack stack = event.getTargetEntity().item().get().createStack();
            List<Text> itemLore = new ArrayList<>();
            
            if(!stack.isEmpty())
            {
            	String id = getID(stack);
            	if ((stack.get(Keys.ITEM_LORE).isPresent())) 
                {
                    for (Text t : getLore(stack)) 
                    {
                    	if(t.toPlain().equals("Bound to: none"))
                    	{
                    		loreAdd(itemLore, player, stack);
                    		event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                    		break;
                    	}
                    	else if (t.toPlain().startsWith("UUID: ")) 
                        {
                            String UUID = t.toPlain().substring(6);
                            if (!UUID.equals(player.getUniqueId().toString())) 
                            {
                                errorMessage(player);
                                event.setCancelled(true);
                            }
                            break;
                        }
                        if (t.toPlain().equals(getLore(stack).get(getLore(stack).size() - 1).toPlain())) 
                        {
                            loreAdd(itemLore, player, stack);
                            event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                        }
                    }
                }
                else if (Reference.sb_pickup.contains(id)) 
                {
                    loreAdd(itemLore, player, stack);
                    event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                }
            }
        }
    }
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
public void onCraft(CraftItemEvent.Preview event, @Root Player player){
    if((player.hasPermission(Reference.CRAFT) || !Reference.craft_perm)){
        if(!event.getPreview().getFinal().isEmpty()) {
            ItemStack stack = event.getPreview().getFinal().createStack();
            String id = getID(stack);
            if (Reference.sb_craft.contains(id)) {
                List<Text> itemLore = new ArrayList<>();
                loreAdd(itemLore, player, stack);
                event.getPreview().setCustom(stack);
            }
        }
    }
}

   @Listener
   public void onDeath(final DropItemEvent.Destruct event, @First Player player) {

	    if(player.hasPermission(Reference.KEEP_ON_DEATH) || !Reference.keep_perm) {

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