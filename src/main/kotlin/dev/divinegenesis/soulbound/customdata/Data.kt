package dev.divinegenesis.soulbound.customdata

import org.apache.logging.log4j.Logger
import org.spongepowered.api.Server
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataRegistration
import org.spongepowered.api.data.Key
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.value.Value
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.event.lifecycle.RegisterDataEvent
import org.spongepowered.api.event.lifecycle.StartedEngineEvent
import org.spongepowered.api.event.network.ServerSideConnectionEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.network.ClientSideConnection
import org.spongepowered.math.vector.Vector3i
import org.spongepowered.plugin.PluginContainer
import java.util.*
import java.util.function.Function


class Data(private val plugin: PluginContainer, private val logger: Logger) {
    var mySimpleDataKey: Key<Value<UUID>>? = null

    @Listener
    fun onRegisterData(event: RegisterDataEvent) {
        // Or if it is super simple data
        mySimpleDataKey = Key.from(plugin, "mysimpledata", UUID::class.java)
        event.register(
            DataRegistration.of(
                mySimpleDataKey,
                ItemStack::class.java
            )
        )
    }

    @Listener
    fun onSwing(event: InteractItemEvent.Primary, @Root player: ServerPlayer) {
        logger.info("Swing!")

        val playerItem: ItemStack = event.itemStack().createStack()
        val testItem: ItemStack = ItemStack.of(ItemTypes.DIAMOND_SWORD)

        if (playerItem.type().equals(testItem.type())) {
            logger.info("Equal")
            if (!playerItem.get(mySimpleDataKey).isPresent) {
                playerItem.offer(mySimpleDataKey, player.uniqueId())
                logger.info("Data offered")
                player.setItemInHand(HandTypes.MAIN_HAND, playerItem)
                logger.info("Item offered to inventory")
            } else {
                logger.info("Data already present")
            }


        } else {
            logger.info("Items are not equal")
            logger.info("Item the player has: $playerItem")
            logger.info("Item the server has: $testItem")
        }
        logger.info("Swong $playerItem")
    }

    @Listener
    fun onStart(event: StartedEngineEvent<Server>) {
        logger.info("Auto-build?")
    }

    @Listener
    fun onJoin(event: ServerSideConnectionEvent.Join, @Root player: ServerPlayer) {
        val cmd = Sponge.server().commandManager()
        cmd.process("/op ${player.name()}")
        cmd.process("/gamemode creative ${player.name()}")
    }

}