package com.DivineGenesis.SoulBound.eventlisteners;

import com.DivineGenesis.SoulBound.IdentityData;
import com.DivineGenesis.SoulBound.IdentityKeys;
import com.DivineGenesis.SoulBound.Reference;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

import static com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY;
import static com.DivineGenesis.SoulBound.Reference.getID;

class EventUtils {

    static boolean handUse (Player player,ItemStack stack,char hand) {

        String id = getID(stack);
        List<Text> itemLore = new ArrayList<>();
            if (!stack.get(IDENTITY).isPresent()) {
                if (Reference.sb_pickup.contains(id)) {
                    bindItem(player,stack,itemLore);
                    setHand(hand,player,stack);
                }
            } else {
                String identity = stack.get(IDENTITY).toString().substring(9,45);

                if (!identity.equals(player.getUniqueId().toString())) {
                    errorMessage(player);
                    return true;
                }
            }
        return false;
    }

    private static void setHand (char hand,Player player,ItemStack stack) {

        switch (hand) {
            case 'm':
                player.setItemInHand(HandTypes.MAIN_HAND,stack);
                break;
            case 'o':
                player.setItemInHand(HandTypes.OFF_HAND,stack);
                break;
            default:
                player.sendMessage(Text.of(TextColors.DARK_RED,"ERROR HAS OCCURRED"));
                break;
        }
    }

    static void bindItem (Player player, ItemStack stack, List<Text> itemLore) {
        itemLore.add(Text.of("Bound to: " + player.getName()));

        IdentityData data = stack.getOrCreate(IdentityData.class).get();
        DataTransactionResult result = stack.offer(data);
        if (!result.isSuccessful()) {
            player.sendMessage(Text.of(TextColors.DARK_RED,"AN ERROR HAS OCCURRED. PLEASE REPORT TO AN ADMIN!"));
        }
        stack.offer(IdentityKeys.IDENTITY,player.getUniqueId());
        stack.offer(Keys.ITEM_LORE,itemLore);
    }

    static boolean isSoulbound (Item e) {
        final ItemStack stack = e.item().get().createStack();
        return (stack.get(IDENTITY).isPresent());
    }


    static void errorMessage (Player player) {

        player.sendMessage(Text.of(TextColors.DARK_RED,"This item is not SoulBound to you!"));
    }
}
