package dev.divinegenesis.soulbound.api

import dev.divinegenesis.soulbound.customdata.Data
import org.spongepowered.api.data.DataTransactionResult
import org.spongepowered.api.item.inventory.ItemStack
import java.lang.Exception
import java.util.*

interface Item {

    companion object {
        fun applyData(itemStack: ItemStack, uuid: UUID): DataTransactionResult? {
            return itemStack.offer(Data.identityDataKey, uuid)
        }

        private fun hasData(itemStack: ItemStack): Boolean {

            return itemStack.get(Data.identityDataKey).isPresent
        }

        fun removeData(itemStack: ItemStack): DataTransactionResult? {
            return itemStack.remove(Data.identityDataKey)
        }
    }

}