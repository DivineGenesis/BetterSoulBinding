package xyz.divinegenesis.soulbound.eventlisteners;


import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.ExpireEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.CraftItemEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.item.inventory.UpdateAnvilEvent;
import org.spongepowered.api.event.user.PardonUserEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.AnvilCost;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import xyz.divinegenesis.soulbound.Main;
import xyz.divinegenesis.soulbound.Reference;
import xyz.divinegenesis.soulbound.data.IdentityKeys;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class EventListener {

    private final Main plugin = Main.getInstance();

    private final EventUtils eventUtils = EventUtils.getEventUtils();

    private final Reference reference = Reference.getReference();

    @Listener
    public void onPickup (ChangeInventoryEvent.Pickup.Pre event, @First Player player) {

        if (player.hasPermission(reference.PICKUP) || !plugin.getSBConfig().pickup.PickupPermissions) {

            if (plugin.getSBConfig().modules.DebugInfo) {
                eventUtils.debugInfo(event);
            }

            Item item = event.getTargetEntity();
            ItemStack stack = item.item().get().createStack();
            Optional<UUID> identity = stack.get(IdentityKeys.IDENTITY);
            String id = eventUtils.getID(stack);

            if (!identity.isPresent()) {
                if (plugin.getSBConfig().pickup.BindOnPickup.contains(id)) {
                    eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                    item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                }
            } else if (!identity.get().equals(player.getUniqueId())) {
                if (identity.get().equals(reference.Blank_UUID)) {
                    stack.remove(IdentityKeys.IDENTITY);
                    eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                    item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                } else {

                    if (!player.hasPermission(reference.BYPASS)) {
                        player.sendMessage(Text.of(TextColors.DARK_RED, plugin.getMessagesConfig().items.ITEM_NOT_BOUND_TO_PLAYER));
                        event.setCancelled(true);
                    } else {
                        player.sendMessage(Text.of(TextColors.YELLOW, plugin.getMessagesConfig().items.ADMIN_BYPASS));
                    }
                }
            }
        }
    }

    @Listener
    public void onEquip (ChangeInventoryEvent.Equipment event, @Root Player player) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }
    }

    @Listener
    public void onHandUse (InteractItemEvent event, @Root Player player) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }

        if (player.hasPermission(reference.USE) || !plugin.getSBConfig().use.UsePermissions) {
            HandType hand;

            if (event instanceof InteractItemEvent.Primary.MainHand || event instanceof InteractItemEvent.Secondary.MainHand) {
                hand = HandTypes.MAIN_HAND;
            } else {
                hand = HandTypes.OFF_HAND;
            }
            ItemStack stack = event.getItemStack().createStack();

            event.setCancelled(eventUtils.handUse(player, stack, hand));
        }
    }

    @Listener
    public void onCraft (CraftItemEvent.Preview event, @Root Player player) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }

        if (player.hasPermission(reference.CRAFT) || !plugin.getSBConfig().craft.CraftPermissions) {
            if (!event.getPreview().getFinal().isEmpty()) {
                ItemStack stack = event.getPreview().getFinal().createStack();
                String id = eventUtils.getID(stack);
                if (plugin.getSBConfig().craft.BindOnCraft.contains(id)) {
                    eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                    event.getPreview().setCustom(stack);

                }
            }
        }
    }

    @Listener
    public void onInventoryClick (ClickInventoryEvent event, @Root Player player) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }

        if (player.hasPermission(reference.INVENTORY) || !plugin.getSBConfig().transaction.TransactionPermissions) {
            if (event instanceof ClickInventoryEvent.Shift || event instanceof ClickInventoryEvent.Drop && (!(event instanceof ClickInventoryEvent.Drop.Outside))) {
                if (!event.getTransactions().isEmpty()) {
                    if (!(event instanceof ClickInventoryEvent.Drop)) {

                        if (event.getTransactions().get(0).getOriginal().getType().equals(ItemTypes.AIR)) {
                            return;
                        }

                        ItemStack stack = event.getTransactions().get(1).getFinal().createStack();
                        if (eventUtils.bypassCheck(stack, player)) {
                            event.setCancelled(true);
                        } else {
                            if (eventUtils.inventoryBind(stack)) {
                                eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                                event.getTransactions().get(1).setCustom(stack);
                            }
                        }
                    }

                } else {
                    ItemStack stack = event.getTransactions().get(0).getOriginal().createStack();
                    if (eventUtils.bypassCheck(stack, player)) {
                        event.setCancelled(true);
                    } else {
                        if (eventUtils.inventoryBind(stack)) {
                            eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                            event.getTransactions().get(1).setCustom(stack);
                        }
                    }
                }
            }
        } else {
            ItemStack stack = event.getCursorTransaction().getFinal().createStack();
            if (eventUtils.bypassCheck(stack, player)) {
                event.setCancelled(true);
            } else {
                if (eventUtils.inventoryBind(stack)) {
                    eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                    event.getTransactions().get(1).setCustom(stack);
                }
            }
        }

    }


    //https://github.com/SpongePowered/SpongeCommon/issues/2426
    @Listener
    public void onAnvilUse (UpdateAnvilEvent event) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }

        Optional<User> user = event.getContext().get(EventContextKeys.OWNER);
        if (user.isPresent()) {
            Optional<Player> player = user.get().getPlayer();
            if (player.isPresent()) {
                if (player.get().hasPermission(reference.ENCHANT) || !plugin.getSBConfig().enchant.enchantPermissions) {
                    if (!event.getRight().isEmpty()) {
                        String bindItemID = eventUtils.getID(event.getRight().createStack());
                        String itemToBeBoundID = eventUtils.getID(event.getLeft().createStack());
                        if (plugin.getSBConfig().enchant.BindOnEnchant.contains(itemToBeBoundID)) {
                            if (bindItemID.equals(plugin.getSBConfig().enchant.BindingItem)) {
                                ItemStack stack = event.getLeft().createStack();
                                eventUtils.bindItem(player.get(), stack, eventUtils.getItemLore());
                                event.getResult().setCustom(stack.createSnapshot());
                                event.getResult().setValid(true);

                                AnvilCost anvilCost = new AnvilCost() {

                                    @Override
                                    public int getContentVersion () {

                                        return 0;
                                    }

                                    @Override
                                    public DataContainer toContainer () {

                                        return null;
                                    }

                                    @Override
                                    public int getLevelCost () {

                                        return 5;
                                    }

                                    @Override
                                    public int getMaterialCost () {

                                        return 1;
                                    }

                                    @Override
                                    public AnvilCost withLevelCost (int levelCost) {

                                        return null;
                                    }

                                    @Override
                                    public AnvilCost withMaterialCost (int materialCost) {

                                        return null;
                                    }
                                };
                                event.getCosts().setCustom(anvilCost);
                            }
                        }
                    }
                }
            }
        }
    }

    @Listener
    public void itemEntityClear (ExpireEntityEvent.TargetItem event) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }

        Optional<User> user = event.getContext().get(EventContextKeys.OWNER);
        if (user.isPresent()) {
            Optional<Player> player = user.get().getPlayer();
            if (player.isPresent()) {
                if (player.get().hasPermission(reference.PREVENT_CLEAR) || !plugin.getSBConfig().modules.preventClearPermissions) {
                    Item e = event.getTargetEntity();
                    ItemStack stack = e.item().get().createStack();

                    if (stack.get(IdentityKeys.IDENTITY).isPresent()) {
                        Location<World> location = event.getTargetEntity().getLocation();
                        Entity item = location.createEntity(EntityTypes.ITEM);
                        item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                        location.spawnEntity(item);
                    }
                }
            }
        }
    }

    @Listener
    public void onDeath (final DropItemEvent.Destruct event, @First Player player) {

        if (plugin.getSBConfig().modules.DebugInfo) {
            eventUtils.debugInfo(event);
        }

        if (player.hasPermission(reference.KEEP_ON_DEATH) || !plugin.getSBConfig().modules.KeepItemsOnDeath) {
            //e.item().get().createStack().get(IDENTITY).isPresent();
            final List<? extends Entity> soulboundItems = event.filterEntities(entity->!(entity instanceof Item) || !((Item) entity).item()
                    .get()
                    .createStack()
                    .get(IdentityKeys.IDENTITY)
                    .isPresent());

            soulboundItems.stream()
                    .map(Item.class::cast)
                    .map(Item::item)
                    .map(BaseValue::get)
                    .map(ItemStackSnapshot::createStack)
                    .forEach(itemStack->player.getInventory().offer(itemStack));
        }
    }

}