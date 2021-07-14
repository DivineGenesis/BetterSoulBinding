package dev.divinegenesis.soulbound.customdata

import dev.divinegenesis.soulbound.api.Item
import org.apache.logging.log4j.Logger
import org.spongepowered.api.data.DataRegistration
import org.spongepowered.api.data.Key
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.value.Value
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.event.lifecycle.RegisterDataEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.network.ClientSideConnection
import org.spongepowered.math.vector.Vector3i
import org.spongepowered.plugin.PluginContainer
import java.util.*
import java.util.function.Function


class Data(private val plugin: PluginContainer, private val logger: Logger) {

    companion object {
        var identityDataKey: Key<Value<UUID>>? = null
    }

    @Listener
    fun onRegisterData(event: RegisterDataEvent) {
        identityDataKey = Key.from(plugin, "identityDataKey", UUID::class.java)
        event.register(
            DataRegistration.of(
                identityDataKey,
                ItemStack::class.java
            )
        )
    }

    @Listener
    fun onSwing(event: InteractItemEvent.Primary, @Root player: ServerPlayer) {
        val playerItem: ItemStack = event.itemStack().createStack()
        val testItem: ItemStack = ItemStack.of(ItemTypes.DIAMOND_SWORD)

        if (playerItem.type().equals(testItem.type())) {
            //Items are the same
            Item.applyData(playerItem, player.uniqueId())
            player.setItemInHand(HandTypes.MAIN_HAND, playerItem)

        } else {
            //Items are not equal at all, Nothing should happen here
            logger.info("Items are not equal")
            logger.info("Item the player has: $playerItem")
            logger.info("Item the server has: $testItem")
        }
    }
}