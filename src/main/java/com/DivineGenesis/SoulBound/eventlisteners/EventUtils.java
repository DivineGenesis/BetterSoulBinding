package com.DivineGenesis.SoulBound.eventlisteners;


import com.DivineGenesis.SoulBound.IdentityData;
import com.DivineGenesis.SoulBound.Messages;
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
import static com.DivineGenesis.SoulBound.Reference.*;


public class EventUtils {

    static boolean handUse (Player player,ItemStack stack,char hand) {

        String id = getID(stack);
        List<Text> itemLore = new ArrayList<>();
        if (!stack.get(IDENTITY).isPresent()) {
            if (Reference.sb_use.contains(id)) {
                bindItem(player,stack,itemLore);
                setHand(hand,player,stack);
            }
        } else {
            if (!stack.get(IDENTITY).get().equals(player.getUniqueId())) {
                if (stack.get(IDENTITY).get().equals(Blank_UUID))
                    player.sendMessage(Text.of(TextColors.RED,Messages.ITEM_NOT_BOUND_TO_PLAYER));
                return true;
            }
        }
        return false;
    }

    static void setHand (char hand,Player player,ItemStack stack) {

        switch (hand) {
            case 'm':
                player.setItemInHand(HandTypes.MAIN_HAND,stack);
                break;
            case 'o':
                player.setItemInHand(HandTypes.OFF_HAND,stack);
                break;
            default:
                player.sendMessage(Text.of(TextColors.DARK_RED,Messages.PLUGIN_ERROR));
                break;
        }
    }

    public static void applyData (Player player,ItemStack stack) {

        IdentityData data = stack.getOrCreate(IdentityData.class).get();
        DataTransactionResult result = stack.offer(data);
        if (!result.isSuccessful()) {
            player.sendMessage(Text.of(TextColors.DARK_RED,Messages.PLUGIN_ERROR));
        }

    }

    static void bindItem (Player player,ItemStack stack,List<Text> itemLore) {

        applyData(player,stack);
        itemLore.add(Text.of(sb_message + player.getName()));

        stack.offer(IDENTITY,player.getUniqueId());
        stack.offer(Keys.ITEM_LORE,itemLore);
    }

    static boolean isSoulbound (Item e) {

        final ItemStack stack = e.item().get().createStack();
        return (stack.get(IDENTITY).isPresent());
    }
}
