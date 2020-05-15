package xyz.divinegenesis.soulbound.eventlisteners;


import xyz.divinegenesis.soulbound.Main;
import xyz.divinegenesis.soulbound.Reference;
import io.github.nucleuspowered.nucleus.api.events.NucleusKitEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import xyz.divinegenesis.soulbound.data.IdentityKeys;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


public class NucleusEventListener {

    private static final Main plugin = Main.getInstance();

    private EventUtils eventUtils = EventUtils.getEventUtils();

    private Reference reference = Reference.getReference();

    @Listener
    public void onKitRedeem (NucleusKitEvent.Redeem.Pre event) {

        Player player = event.getTargetEntity();

        ArrayList<ItemStackSnapshot> newStacks = new ArrayList<>(event.getOriginalStacksToRedeem());

        for (ItemStackSnapshot stackSnapshot : event.getOriginalStacksToRedeem()) {

            ItemStack stack = stackSnapshot.createStack();

            Optional<UUID> identity = stack.get(IdentityKeys.IDENTITY);


            if (identity.isPresent()) {
                if (identity.get().equals(reference.Blank_UUID)) {
                    newStacks.remove(stackSnapshot);
                    stack.remove(IdentityKeys.IDENTITY);
                    eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                    newStacks.add(stack.createSnapshot());
                }
            }

            String id = eventUtils.getID(stack);
            if (plugin.getSBConfig().nucleus.BindOnRedeem.contains(id) || plugin.getSBConfig().nucleus.BindKitItems) {

                newStacks.remove(stackSnapshot);
                eventUtils.bindItem(player, stack, eventUtils.getItemLore());
                newStacks.add(stack.createSnapshot());
            }
        }
        event.setStacksToRedeem(newStacks);
    }

}
