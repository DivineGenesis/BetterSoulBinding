package xyz.divinegenesis.soulbound.eventlisteners;


import org.slf4j.Logger;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import xyz.divinegenesis.soulbound.Main;
import xyz.divinegenesis.soulbound.Reference;
import xyz.divinegenesis.soulbound.data.IdentityData;
import xyz.divinegenesis.soulbound.data.IdentityKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class EventUtils {

    private final Main plugin = Main.getInstance();

    private final Reference reference = Reference.getReference();

    private List<Text> itemLore = new ArrayList<>();

    private static EventUtils eventUtils;

    public EventUtils () {
        eventUtils = this;
    }

    boolean handUse (Player player, ItemStack stack, HandType hand) {

        String id = getID(stack);

        if (!stack.get(IdentityKeys.IDENTITY).isPresent()) {
            if (plugin.getSBConfig().use.BindOnUse.contains(id)) {
                bindItem(player, stack, getItemLore());
                setHand(hand, player, stack);
            }
        } else {
            if (!stack.get(IdentityKeys.IDENTITY).get().equals(player.getUniqueId())) {
                if (stack.get(IdentityKeys.IDENTITY).get().equals(reference.Blank_UUID)) {
                    if (!player.hasPermission(reference.BYPASS)) {  //Fix issue with no message returning
                        player.sendMessage(Text.of(TextColors.RED, plugin.getMessagesConfig().items.ITEM_NOT_BOUND_TO_PLAYER));
                        return true;
                    } else {
                        player.sendMessage(Text.of(plugin.getMessagesConfig().items.ADMIN_BYPASS));
                    }
                }
            }
        }
        return false;
    }

    private void setHand (HandType hand, Player player, ItemStack stack) {


        if (hand.equals(HandTypes.MAIN_HAND)) {
            player.setItemInHand(HandTypes.MAIN_HAND, stack);
        } else if (hand.equals(HandTypes.OFF_HAND)) {
            player.setItemInHand(HandTypes.OFF_HAND, stack);
        } else { //Should never be reached, this is just in case mods do something silly
            player.sendMessage(Text.of(Text.of(plugin.getMessagesConfig().PLUGIN_ERROR)));
        }
    }

    public void applyData (Player player, ItemStack stack) {

        IdentityData data = stack.getOrCreate(IdentityData.class).get();
        DataTransactionResult result = stack.offer(data);
        if (!result.isSuccessful()) {
            player.sendMessage(Text.of(TextColors.DARK_RED, plugin.getMessagesConfig().PLUGIN_ERROR));
        }
    }

    void bindItem (Player player, ItemStack stack, List<Text> itemLore) {

        applyData(player, stack);
        itemLore.add(Text.of(plugin.getMessagesConfig().BIND_MESSAGE + player.getName()));

        stack.offer(IdentityKeys.IDENTITY, player.getUniqueId());
        stack.offer(Keys.ITEM_LORE, itemLore);

        getItemLore().clear();
    }

    void debugInfo (Event event) {

        Logger logger = plugin.getLogger();

        logger.debug("\n[DEBUG]\n" + "\n[CLASS]: " + event.getClass() + "\n[CAUSE]: " + event.getCause() + "\n[CONTEXT]: " + event.getContext() + "\n[SOURCE]: " + event
                .getSource());

    }


    public String getID (ItemStack stack) {

        String ID = stack.getType().getId();
        DataContainer container = stack.toContainer();
        DataQuery data = DataQuery.of('/', "UnsafeDamage");
        int meta = Integer.parseInt(container.get(data).get().toString());
        if (meta != 0 && stack.getValue(Keys.UNBREAKABLE).isPresent()) {
            if (stack.getValue(Keys.UNBREAKABLE).get().get()) {
                ID += ":" + meta;
            }
        }
        return ID;
    }

    boolean inventoryBind (ItemStack stack) {

        return !stack.get(IdentityKeys.IDENTITY).isPresent() && plugin.getSBConfig().transaction.BindOnInventoryTransaction.contains(getID(stack));
    }

    boolean bypassCheck (ItemStack stack, Player player) {

        Optional<UUID> optionalUuid = stack.get(IdentityKeys.IDENTITY);

        if (optionalUuid.isPresent()) {
            if (!optionalUuid.get().equals(player.getUniqueId())) {

                if (!player.hasPermission(reference.BYPASS)) {
                    player.sendMessage(Text.of(TextColors.DARK_RED, plugin.getMessagesConfig().items.ITEM_NOT_BOUND_TO_PLAYER));
                    return true;

                } else {
                    player.sendMessage(Text.of(TextColors.YELLOW, plugin.getMessagesConfig().items.ADMIN_BYPASS));
                    return false;
                }

            }
        }
        return false;
    }

    List<Text> getItemLore () {

        return itemLore;
    }

    public static EventUtils getEventUtils () {

        return eventUtils;
    }
}
