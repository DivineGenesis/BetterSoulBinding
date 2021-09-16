package dev.divinegenesis.soulbound

import dev.divinegenesis.soulbound.customdata.*
import org.spongepowered.api.data.Keys
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.value.Value
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.Item
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.EventContextKeys
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.entity.ExpireEntityEvent
import org.spongepowered.api.event.entity.InteractEntityEvent
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent
import org.spongepowered.api.event.item.inventory.CraftItemEvent
import org.spongepowered.api.event.item.inventory.DropItemEvent
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import kotlin.math.abs


class EventListener {

    //I'm honestly throwing caution to the wind here
    private val config = Soulbound.instance.config.get()!!.modules

    @Listener(order = Order.FIRST)
    fun onPickup(event: ChangeInventoryEvent.Pickup.Pre, @First player: ServerPlayer) {
        val originalStack = event.originalStack().createStack()
        val bindItem = Soulbound.instance.database[originalStack.getID()]?.pickup ?: 0

        if (!config.pickupItemModule || !bindItem.toBool()) {
            return
        }

        val dataPair = DataUtilities().sortData(originalStack, player.uniqueId())
        event.isCancelled = dataPair.cancelEvent()

        event.setCustom(mutableListOf(dataPair.stack().createSnapshot()))
    }

    @Listener(order = Order.FIRST)
    fun onInteractBlock(event: InteractBlockEvent.Primary.Start, @Root player: ServerPlayer) {
        val originalStack = event.context().get(EventContextKeys.USED_ITEM).get().createStack()
        val bindItem = Soulbound.instance.database[originalStack.getID()]?.interact ?: 0

        if (!config.interactItemModule || !bindItem.toBool()) {
            return
        }

        val dataPair = DataUtilities().sortData(originalStack, player.uniqueId())
        event.isCancelled = dataPair.cancelEvent()

        player.setItemInHand(HandTypes.MAIN_HAND, dataPair.stack())
    }

    @Listener(order = Order.FIRST)
    fun onInteractEntity(event: InteractEntityEvent.Primary, @Root player: ServerPlayer) {
        val originalStack = event.context().get(EventContextKeys.USED_ITEM).get().createStack()
        val bindItem = Soulbound.instance.database[originalStack.getID()]?.interact ?: 0

        if (!config.interactItemModule || !bindItem.toBool()) {
            return
        }

        val dataPair = DataUtilities().sortData(originalStack, player.uniqueId())
        event.isCancelled = dataPair.cancelEvent()

        player.setItemInHand(HandTypes.MAIN_HAND, dataPair.stack())
    }

    @Listener(order = Order.FIRST)
    fun onInteractItem(event: InteractItemEvent, @Root player: ServerPlayer) {
        val originalStack = event.itemStack().createStack()
        val bindItem = Soulbound.instance.database[originalStack.getID()]?.interact ?: 0

        if (!config.interactItemModule || !bindItem.toBool()) {
            return
        }

        val dataPair = DataUtilities().sortData(originalStack, player.uniqueId())

        //Primary cannot be cancelled
        //However we still want primary to be bound
        if (event is InteractItemEvent.Secondary) {
            event.isCancelled = dataPair.cancelEvent()
        }

        player.setItemInHand(HandTypes.MAIN_HAND, dataPair.stack())
    }

    @Listener(order = Order.FIRST)
    fun onCraft(event: CraftItemEvent.Preview, @Root player: ServerPlayer) {
        val originalStack = event.preview().finalReplacement().createStack()
        val bindItem = Soulbound.instance.database[originalStack.getID()]?.craft ?: 0

        if (!config.craftItemModule || !bindItem.toBool()) {
            return
        }

        val dataPair = DataUtilities().sortData(originalStack, player.uniqueId())

        event.preview().setCustom(dataPair.stack())
    }

    @Suppress("UNUSED_PARAMETER")
    @Listener
    fun itemEntityClear(event: ExpireEntityEvent, @Getter("entity") entity: Entity) {
        if (!config.itemDeSpawnModule) {
            return
        }

        if (entity.type() == EntityTypes.ITEM) {
            val stack = entity[Keys.ITEM_STACK_SNAPSHOT].get().createStack()
            if (stack.containsData()) {
                val location = entity.location()
                location.world().spawnEntity(entity)
            }
        }
    }

    @Listener
    fun onDeath(event: DropItemEvent.Destruct, @First player: ServerPlayer) {
        if (!config.onDeathModule) {
            return
        }

        val soulboundItems: List<Entity?> = event.filterEntities { entity ->
            entity !is Item || !entity.item()
                .get()
                .createStack()
                .containsData()
        }
        soulboundItems.stream()
            .map(Item::class.java::cast)
            .map(Item::item)
            .map(Value<ItemStackSnapshot>::get)
            .map(ItemStackSnapshot::createStack)
            .forEach {
                player.inventory().offer(it)
            }
    }

}
/*

        @Listener
        fun onInventoryClick(event: ClickInventoryEvent, @Root player: Player) {
            if (plugin.getSBConfig().modules.DebugInfo) {
                eventUtils.debugInfo(event)
            }
            if (player.hasPermission(reference.INVENTORY) || !plugin.getSBConfig().transaction.TransactionPermissions) {
                if (event is ClickInventoryEvent.Shift || event is ClickInventoryEvent.Drop && event !is ClickInventoryEvent.Drop.Outside) {
                    if (!event.getTransactions().isEmpty()) {
                        if (event !is ClickInventoryEvent.Drop) {
                            if (event.getTransactions().get(0).getOriginal().getType().equals(ItemTypes.AIR)) {
                                return
                            }
                            val stack: ItemStack = event.getTransactions().get(1).getFinal().createStack()
                            if (eventUtils.bypassCheck(stack, player)) {
                                event.setCancelled(true)
                            } else {
                                if (eventUtils.inventoryBind(stack)) {
                                    eventUtils.bindItem(player, stack, eventUtils.getItemLore())
                                    event.getTransactions().get(1).setCustom(stack)
                                }
                            }
                        }
                    } else {
                        val stack: ItemStack = event.getTransactions().get(0).getOriginal().createStack()
                        if (eventUtils.bypassCheck(stack, player)) {
                            event.setCancelled(true)
                        } else {
                            if (eventUtils.inventoryBind(stack)) {
                                eventUtils.bindItem(player, stack, eventUtils.getItemLore())
                                event.getTransactions().get(1).setCustom(stack)
                            }
                        }
                    }
                }
            } else {
                val stack: ItemStack = event.getCursorTransaction().getFinal().createStack()
                if (eventUtils.bypassCheck(stack, player)) {
                    event.setCancelled(true)
                } else {
                    if (eventUtils.inventoryBind(stack)) {
                        eventUtils.bindItem(player, stack, eventUtils.getItemLore())
                        event.getTransactions().get(1).setCustom(stack)
                    }
                }
            }
        }

        //https://github.com/SpongePowered/SpongeCommon/issues/2426
        @Listener
        fun onAnvilUse(event: UpdateAnvilEvent) {
            if (plugin.getSBConfig().modules.DebugInfo) {
                eventUtils.debugInfo(event)
            }
            val user: Optional<User> = event.getContext().get(EventContextKeys.OWNER)
            if (user.isPresent()) {
                val player: Optional<Player> = user.get().getPlayer()
                if (player.isPresent()) {
                    if (player.get()
                            .hasPermission(reference.ENCHANT) || !plugin.getSBConfig().enchant.enchantPermissions
                    ) {
                        if (!event.getRight().isEmpty()) {
                            val bindItemID: String = eventUtils.getID(event.getRight().createStack())
                            val itemToBeBoundID: String = eventUtils.getID(event.getLeft().createStack())
                            if (plugin.getSBConfig().enchant.BindOnEnchant.contains(itemToBeBoundID)) {
                                if (bindItemID == plugin.getSBConfig().enchant.BindingItem) {
                                    val stack: ItemStack = event.getLeft().createStack()
                                    eventUtils.bindItem(player.get(), stack, eventUtils.getItemLore())
                                    event.getResult().setCustom(stack.createSnapshot())
                                    event.getResult().setValid(true)
                                    val anvilCost: AnvilCost = object : AnvilCost() {
                                        val contentVersion: Int
                                            get() = 0

                                        fun toContainer(): DataContainer? {
                                            return null
                                        }

                                        val levelCost: Int
                                            get() = 5
                                        val materialCost: Int
                                            get() = 1

                                        fun withLevelCost(levelCost: Int): AnvilCost? {
                                            return null
                                        }

                                        fun withMaterialCost(materialCost: Int): AnvilCost? {
                                            return null
                                        }
                                    }
                                    event.getCosts().setCustom(anvilCost)
                                }
                            }
                        }
                    }
                }
            }
        }


*/

