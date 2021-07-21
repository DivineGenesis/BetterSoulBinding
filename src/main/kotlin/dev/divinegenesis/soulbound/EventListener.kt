package dev.divinegenesis.soulbound

import dev.divinegenesis.soulbound.customdata.Data
import org.apache.logging.log4j.Logger
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.EventContextKeys
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.entity.InteractEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent
import org.spongepowered.api.event.item.inventory.CraftItemEvent
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.item.inventory.ItemStack
import java.lang.NullPointerException
import java.util.*


class EventListener {

    @Listener(order = Order.FIRST)
    fun onPickup(event: ChangeInventoryEvent.Pickup.Pre, @First player: ServerPlayer) {

        val originalStack = event.originalStack().createStack()

        val dataPair = Utils().sortData(originalStack, player.uniqueId())
        val finalStack = dataPair.first.createSnapshot()
        val denyEvent = dataPair.second

        event.isCancelled = denyEvent
        event.setCustom(mutableListOf(finalStack))
    }

    @Listener
    fun onInteractBlock(event: InteractBlockEvent.Primary.Start, @Root player: ServerPlayer) {

        val originalStack = event.context().get(EventContextKeys.USED_ITEM).get().createStack()

        val dataPair = Utils().sortData(originalStack, player.uniqueId())
        val finalStack = dataPair.first
        val denyEvent = dataPair.second

        event.isCancelled = denyEvent
        player.setItemInHand(HandTypes.MAIN_HAND, finalStack)
    }

    @Listener
    fun onInteractEntity(event: InteractEntityEvent.Primary, @Root player: ServerPlayer) {

        val originalStack = event.context().get(EventContextKeys.USED_ITEM).get().createStack()

        val dataPair = Utils().sortData(originalStack, player.uniqueId())
        val finalStack = dataPair.first
        val denyEvent = dataPair.second

        event.isCancelled = denyEvent
        player.setItemInHand(HandTypes.MAIN_HAND, finalStack)
    }

    @Listener
    fun onInteractItemSecondary(event: InteractItemEvent.Secondary, @Root player: ServerPlayer) {

        val originalStack = event.itemStack().createStack()

        val dataPair = Utils().sortData(originalStack, player.uniqueId())
        val finalStack = dataPair.first
        val denyEvent = dataPair.second

        event.isCancelled = denyEvent
        player.setItemInHand(HandTypes.MAIN_HAND, finalStack)
    }

    @Listener
    fun onCraft(event: CraftItemEvent.Preview, @Root player: ServerPlayer) {
        val originalStack = event.preview().finalReplacement().createStack()

        val dataPair = Utils().sortData(originalStack, player.uniqueId())
        val finalStack = dataPair.first

        //Should never need to deny this event.

        event.preview().setCustom(finalStack)
    }
}
/*

        @Listener
        fun onCraft(event: CraftItemEvent.Preview, @Root player: Player) {
            if (plugin.getSBConfig().modules.DebugInfo) {
                eventUtils.debugInfo(event)
            }
            if (player.hasPermission(reference.CRAFT) || !plugin.getSBConfig().craft.CraftPermissions) {
                if (!event.getPreview().getFinal().isEmpty()) {
                    val stack: ItemStack = event.getPreview().getFinal().createStack()
                    val id: String = eventUtils.getID(stack)
                    if (plugin.getSBConfig().craft.BindOnCraft.contains(id)) {
                        eventUtils.bindItem(player, stack, eventUtils.getItemLore())
                        event.getPreview().setCustom(stack)
                    }
                }
            }
        }

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

        @Listener
        fun itemEntityClear(event: ExpireEntityEvent.TargetItem) {
            if (plugin.getSBConfig().modules.DebugInfo) {
                eventUtils.debugInfo(event)
            }
            val user: Optional<User> = event.getContext().get(EventContextKeys.OWNER)
            if (user.isPresent()) {
                val player: Optional<Player> = user.get().getPlayer()
                if (player.isPresent()) {
                    if (player.get()
                            .hasPermission(reference.PREVENT_CLEAR) || !plugin.getSBConfig().modules.preventClearPermissions
                    ) {
                        val e: Item = event.getTargetEntity()
                        val stack: ItemStack = e.item().get().createStack()
                        if (stack.get(IdentityKeys.IDENTITY).isPresent()) {
                            val location: Location<World> = event.getTargetEntity().getLocation()
                            val item: Entity = location.createEntity(EntityTypes.ITEM)
                            item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot())
                            location.spawnEntity(item)
                        }
                    }
                }
            }
        }

        @Listener
        fun onDeath(event: DropItemEvent.Destruct, @First player: Player) {
            if (plugin.getSBConfig().modules.DebugInfo) {
                eventUtils.debugInfo(event)
            }
            if (player.hasPermission(reference.KEEP_ON_DEATH) || !plugin.getSBConfig().modules.KeepItemsOnDeath) {
                //e.item().get().createStack().get(IDENTITY).isPresent();
                val soulboundItems: List<Entity?> = event.filterEntities { entity ->
                    entity !is Item || !(entity as Item).item()
                        .get()
                        .createStack()
                        .get(IdentityKeys.IDENTITY)
                        .isPresent()
                }
                soulboundItems.stream()
                    .map { `object`: Any? -> Item::class.java.cast(`object`) }
                    .map<Any>(Item::item)
                    .map<Any>(BaseValue::get)
                    .map<Any>(ItemStackSnapshot::createStack)
                    .forEach(Consumer { itemStack: Any? ->
                        player.getInventory().offer(itemStack)
                    })
            }
        }
    }
*/

class Utils {

    private val identityDataKey = Data.identityDataKey
    private val blankUUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    private val logger: Logger = logger<Utils>()

    private fun containsData(stack: ItemStack): Boolean {
        logger.info(
            """
            =
            ===================================================
            Stack   :   $stack
            Key     :   $identityDataKey
            Data    :   ${stack.keys.contains(identityDataKey)}
            ===================================================
        """.trimIndent()
        )
        return stack.keys.contains(identityDataKey)
    }

    private fun removeData(stack: ItemStack): Boolean { //return true if operation didn't fail
        return if (containsData(stack)) {
            stack.remove(identityDataKey).isSuccessful
        } else {
            true
        }
    }

    private fun applyData(stack: ItemStack, userUUID: UUID) {
        try {
            stack.offer(identityDataKey, userUUID)

        } catch (e: NullPointerException) {
            logger.error(
                """
                =
                ==============NPE FIRED========================
                Identity Key    :   $identityDataKey
                User UUID       :   $userUUID
                ===============================================
            """.trimIndent()
            )
        }
    }

    fun sortData(stack: ItemStack, userUUID: UUID): Pair<ItemStack, Boolean> {
        if (containsData(stack)) {
            return when (stack.get(identityDataKey).get()) {
                blankUUID -> {
                    removeData(stack)
                    applyData(stack, userUUID)
                    Pair(stack, true)
                }
                userUUID -> {//stack false
                    Pair(stack, false)
                }
                else -> {
                    Pair(stack, true)
                }
            }
        } else {
            applyData(stack, userUUID)
            return Pair(stack, false)
        }
    }
}