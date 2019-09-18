package com.DivineGenesis.SoulBound.eventlisteners;


import com.DivineGenesis.SoulBound.Main;
import io.github.nucleuspowered.nucleus.api.events.NucleusKitEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.util.*;

import static com.DivineGenesis.SoulBound.Reference.Blank_UUID;
import static com.DivineGenesis.SoulBound.Reference.getID;
import static com.DivineGenesis.SoulBound.data.IdentityKeys.IDENTITY;
import static com.DivineGenesis.SoulBound.eventlisteners.EventUtils.bindItem;


public class NucleusEventListener {

    private static final Main plugin = Main.getInstance();

    @Listener
    public void onKitRedeem (NucleusKitEvent.Redeem.Pre event) {

        Player player = event.getTargetEntity();
        List<Text> itemLore = new ArrayList<>();

        Collection<ItemStackSnapshot> newStacks = new ArrayList<>(event.getOriginalStacksToRedeem());

        for (ItemStackSnapshot stackSnapshot : event.getOriginalStacksToRedeem()) {

            ItemStack stack = stackSnapshot.createStack();

            Optional<UUID> identity = stack.get(IDENTITY);

            itemLore.clear();

            if (identity.isPresent()) {
                if (identity.get().equals(Blank_UUID)) {
                    newStacks.remove(stackSnapshot);
                    stack.remove(IDENTITY);
                    bindItem(player, stack, itemLore);
                    newStacks.add(stack.createSnapshot());
                }
            }

            String id = getID(stack);
            if (plugin.getSBConfig().nucleus.BindOnRedeem.contains(id) || plugin.getSBConfig().nucleus.BindKitItems) {

                newStacks.remove(stackSnapshot);
                bindItem(player, stack, itemLore);
                newStacks.add(stack.createSnapshot());
            }
        }
        event.setStacksToRedeem(newStacks);
    }

}
