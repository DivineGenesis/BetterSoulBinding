package com.DivineGenesis.SoulBound.eventlisteners;


import com.DivineGenesis.SoulBound.Main;
import com.DivineGenesis.SoulBound.Reference;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

import static com.DivineGenesis.SoulBound.data.IdentityKeys.IDENTITY;
import static com.DivineGenesis.SoulBound.Reference.Blank_UUID;
import static com.DivineGenesis.SoulBound.Reference.getID;
import static com.DivineGenesis.SoulBound.eventlisteners.EventUtils.*;


public class EventListener {

    private static final Main plugin = Main.getInstance();

    @Listener
    public void onPickup (ChangeInventoryEvent.Pickup.Pre event, @First Player player) {

        if (player.hasPermission(Reference.PICKUP) || !plugin.getSBConfig().modules.PickupPermissions) {

            ItemStack stack = event.getTargetEntity().item().get().createStack();
            String id = getID(stack);
            List<Text> itemLore = new ArrayList<>();
            if (!stack.get(IDENTITY).isPresent()) {
                if (plugin.getSBConfig().BindOnPickup.contains(id)) {
                    bindItem(player, stack, itemLore);
                    event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                }
            } else if (!stack.get(IDENTITY).get().equals(player.getUniqueId())) {
                if (stack.get(IDENTITY).get().equals(Blank_UUID)) {
                    stack.remove(IDENTITY);
                    bindItem(player, stack, itemLore);
                    event.getTargetEntity().offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                } else {
                    player.sendMessage(Text.of(TextColors.RED, plugin.getMessagesConfig().items.ITEM_NOT_BOUND_TO_PLAYER));
                    event.setCancelled(true);
                }
            }
        }
    }

   /* @Listener
    public void onEquip (ChangeInventoryEvent.Equipment event,@First Player player) {

        player.sendMessage(Text.of("Equip event fired"));
        if (player.hasPermission(Reference.EQUIP) || !Reference.equip_perm) {

        }
    }*/

    @Listener
    public void onHandUse (InteractItemEvent event, @Root Player player) {

        if ((player.hasPermission(Reference.USE) || !plugin.getSBConfig().modules.UsePermissions)) {
            char hand;

            if (event instanceof InteractItemEvent.Primary.MainHand || event instanceof InteractItemEvent.Secondary.MainHand)
                hand = 'm';
            else
                hand = 'o';

            ItemStack stack = event.getItemStack().createStack();
            event.setCancelled(handUse(player, stack, hand));
        }
    }

    @Listener
    public void onCraft (CraftItemEvent.Preview event, @Root Player player) {

        if ((player.hasPermission(Reference.CRAFT) || !plugin.getSBConfig().modules.CraftPermissions)) {
            if (!event.getPreview().getFinal().isEmpty()) {
                ItemStack stack = event.getPreview().getFinal().createStack();
                String id = getID(stack);
                if (plugin.getSBConfig().BindOnCraft.contains(id)) {
                    List<Text> itemLore = new ArrayList<>();
                    bindItem(player, stack, itemLore);
                    event.getPreview().setCustom(stack);
                }
            }
        }
    }

    @Listener
    public void onDeath (final DropItemEvent.Destruct event, @First Player player) {

        if (player.hasPermission(Reference.KEEP_ON_DEATH) || !plugin.getSBConfig().modules.KeepItemsOnDeath) {

            final List<? extends Entity> soulboundItems = event.filterEntities(entity->!(entity instanceof Item) || !isSoulbound((Item) entity));

            soulboundItems.stream()
                    .map(Item.class::cast)
                    .map(Item::item)
                    .map(BaseValue::get)
                    .map(ItemStackSnapshot::createStack)
                    .forEach(itemStack->player.getInventory().offer(itemStack));
        }
    }


}