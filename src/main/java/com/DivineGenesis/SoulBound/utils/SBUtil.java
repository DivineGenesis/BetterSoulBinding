package com.DivineGenesis.SoulBound.utils;

import java.util.List;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.DivineGenesis.SoulBound.Messages;

public class SBUtil
{
	private static String UUID = "UUID: ";
    private static String boundTo = "Bound to: none";
    
	public static List<Text> getLore (ItemStack stack) {
		return stack.get(Keys.ITEM_LORE).get();
	}

	public static String getID (ItemStack stack) {
		String ID = stack.getType().getId();
		DataContainer container = stack.toContainer();
		DataQuery data = DataQuery.of('/',"UnsafeDamage");
		int meta = Integer.parseInt(container.get(data).get().toString());
		if (meta != 0 && stack.getValue(Keys.UNBREAKABLE).isPresent()) {
			if (stack.getValue(Keys.UNBREAKABLE).get().get()) {
				ID += ":" + meta;
			}
		}
		return ID;
	}
	
	public static boolean handUse (Player player,ItemStack stack,char hand) {
        List<Text> itemLore;
        if (!isSoulbound(player,stack)) {
            itemLore = getLore(stack);
            loreAdd(player,itemLore,stack);
            setHand(player,hand,stack);
        } else {
            errorMessage(player);
            return true;
        }
        return false;
    }

    public static boolean isSoulbound (Player player,Item item) {
        final ItemStack stack = item.item().get().createStack();
        return isSoulbound(player,stack);
    }

    public static boolean isSoulbound (Player player,ItemStack stack) {
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

    private static void setHand (Player player,char hand,ItemStack stack) {
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

    public static void loreAdd (Player player,List<Text> itemLore,ItemStack stack) {
        itemLore.add(Text.of(boundTo + player.getName()));
        itemLore.add(Text.of(UUID + player.getUniqueId().toString()));
        stack.offer(Keys.ITEM_LORE,itemLore);
    }

    public static void errorMessage (Player player) {
        player.sendMessage(Text.of(TextColors.DARK_RED, Messages.Player.NOT_YOURS));
    }
}
