package dev.divinegenesis.soulbound.api

import dev.divinegenesis.soulbound.customdata.Data
import org.spongepowered.api.data.DataTransactionResult
import org.spongepowered.api.item.inventory.ItemStack
import java.lang.Exception
import java.util.*

interface Item {

    companion object {
        fun applyData(itemStack: ItemStack, uuid: UUID): DataTransactionResult? {
            return if (!hasData(itemStack)) {
                itemStack.offer(Data.identityDataKey,uuid)
            } else { //Already has data
                DataTransactionResult.failNoData()
            }
        }

        private fun hasData(itemStack: ItemStack): Boolean {
            return itemStack.get(Data.identityDataKey).isPresent
        }

        fun removeData(itemStack: ItemStack): DataTransactionResult? {
            return if(hasData(itemStack)) {
                itemStack.remove(Data.identityDataKey)
            } else {
                DataTransactionResult.failNoData()  //Return fail if item doesn't have data
            }
        }
    }

}