package dev.divinegenesis.soulbound.customdata

import dev.divinegenesis.soulbound.Soulbound
import dev.divinegenesis.soulbound.getID
import dev.divinegenesis.soulbound.logger
import org.apache.logging.log4j.Logger
import org.spongepowered.api.item.inventory.ItemStack
import java.lang.NullPointerException
import java.util.*
import dev.divinegenesis.soulbound.customdata.Data.DataKey.identityDataKey as identityDataKey

class DataUtilities {

    private val logger: Logger = logger<DataUtilities>()

    companion object BlankUUID {
        val blankUUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    fun containsData(stack: ItemStack): Boolean {
        return stack.keys.contains(identityDataKey)
    }

    fun removeData(stack: ItemStack): Boolean { //return true if operation didn't fail
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

        if (Soulbound.database.containsKey(stack.getID())) {
            if (containsData(stack)) {
                return when (stack.get(identityDataKey).get()) {
                    blankUUID -> {
                        removeData(stack)
                        applyData(stack, userUUID)
                        Pair(stack, true)
                    }
                    userUUID -> {
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
        return Pair(stack, false)
    }
}

data class DataStack(val itemID: String, var interact: Int = 0, var pickup: Int = 0, var craft: Int = 0)

fun Pair<ItemStack, Boolean>.stack() = first
fun Pair<ItemStack, Boolean>.cancelEvent() = second

fun Boolean.toInt() = compareTo(false)
fun Int.toBool() = this == 1