package com.DivineGenesis.SoulBound.EventListeners;

import java.util.List;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import static com.DivineGenesis.SoulBound.Reference.getLore;
import static com.DivineGenesis.SoulBound.Reference.getID;

class EventUtils {
    private static String UUID = "UUID: ";
    private static String boundTo = "Bound to: none";

    static boolean handUse(Player player, ItemStack stack, char hand) {
        List<Text> itemLore;
            if(!isSoulbound(player,stack)){
                itemLore = getLore(stack);
                loreAdd(player,itemLore,stack);
                setHand(player,hand,stack);
            }else {
                errorMessage(player);
                return true;
            }
        return false;
    }

    static boolean isSoulbound(Player player, Item item) {
        final ItemStack stack = item.item().get().createStack();
        return isSoulbound(player, stack);
    }
    static boolean isSoulbound(Player player, ItemStack stack) {
        String id = getID(stack);
         if (stack.get(Keys.ITEM_LORE).isPresent()) {
             for (Text t : getLore(stack)) {
                    if (t.toPlain().equals(boundTo)) {
                        return false;
                    } else if (t.toPlain().startsWith(UUID)) {
                     String UUID = t.toPlain().substring(6);
                     return UUID.equals(player.getUniqueId().toString());
                    }
                if (t.toPlain().equals(getLore(stack).get(getLore(stack).size() - 1).toPlain()))
                    return false;
             }
         }
         return false;
    }

    private static void setHand(Player player, char hand,  ItemStack stack) {
        switch (hand) {
            case 'm':
                player.setItemInHand(HandTypes.MAIN_HAND, stack);
                break;
            case 'o':
                player.setItemInHand(HandTypes.OFF_HAND, stack);
                break;
            default:
                player.sendMessage(Text.of(TextColors.DARK_RED, "ERROR HAS OCCURRED"));
                break;
        }
    }

    static void loreAdd(Player player, List<Text> itemLore,  ItemStack stack) {
        itemLore.add(Text.of(boundTo + player.getName()));
        itemLore.add(Text.of(UUID + player.getUniqueId().toString()));
        stack.offer(Keys.ITEM_LORE, itemLore);
    }

    static void errorMessage(Player player) {
        player.sendMessage(Text.of(TextColors.DARK_RED, "This item is not soulbound to you!"));
    }
}
