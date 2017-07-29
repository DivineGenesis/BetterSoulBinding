package com.gmail.drzoddiak.soulbound;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;

public class EventListener {

    private void onHold(ChangeInventoryEvent.Held event, @First Player player){

    }

    private void onPickup(ChangeInventoryEvent.Pickup event, @First Player player){

    }

    private void onEquip(ChangeInventoryEvent.Equipment event, @First Player player){
        //May be a better way to do this
    }

    private void onUse(InteractItemEvent.Primary.MainHand event, @First Player player){

    }

    private void onSecUse(InteractItemEvent.Primary.OffHand event, @First Player player){

    }

    private void configCheck(String itemToCheck, Player player){

    }

}
