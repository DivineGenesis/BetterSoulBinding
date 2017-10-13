package com.gmail.drzoddiak.soulbound.EventListeners;

import com.gmail.drzoddiak.soulbound.Reference;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.drzoddiak.soulbound.EventListeners.EventUtils.loreAdd;
import static com.gmail.drzoddiak.soulbound.Reference.getLore;

class EventUtils {

    static boolean handUse(Player player, ItemStack stack, char hand) 
    {
        String id = Reference.getID(stack);
        List<Text> itemLore = new ArrayList<>();
        if ((stack.get(Keys.ITEM_LORE).isPresent())) 
        {
            for (Text t : getLore(stack)) 
            {
            	if(t.toPlain().equals("Bound to: none"))
            	{
            		loreAdd(itemLore, player, stack);
            		setHand(hand, player, stack);
            		break;
            	}
                if (t.toPlain().startsWith("UUID: ")) 
                {
                    String UUID = t.toPlain().substring(6);
                    if (!UUID.equals(player.getUniqueId().toString())) 
                    {
                        errorMessage(player);
                        return true;
                    }
                    break;
                }
                if (t.toPlain().equals(getLore(stack).get(getLore(stack).size() - 1).toPlain())) 
                {
                    itemLore = getLore(stack);
                    loreAdd(itemLore, player, stack);
                    setHand(hand, player, stack);
                }
            }
        } 
        else if (Reference.sb_use.contains(id)) 
        {
            loreAdd(itemLore, player, stack);
            setHand(hand, player, stack);
        }
        return false;
    }

    private static void setHand(char hand, Player player, ItemStack stack) 
    {
        switch (hand) 
        {
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

    static void loreAdd(List<Text> itemLore, Player player, ItemStack stack) 
    {
        itemLore.add(Text.of("Bound to: " + player.getName()));
        itemLore.add(Text.of("UUID: " + player.getUniqueId().toString()));
        stack.offer(Keys.ITEM_LORE, itemLore);
    }

    static void errorMessage(Player player) 
    {
        player.sendMessage(Text.of(TextColors.DARK_RED, "This item is not soulbound to you!"));
    }
}
