package dev.divinegenesis.soulbound.customdata

import dev.divinegenesis.soulbound.logger
import org.spongepowered.api.data.DataRegistration
import org.spongepowered.api.data.Key
import org.spongepowered.api.data.value.Value
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.lifecycle.RegisterDataEvent
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.plugin.PluginContainer
import java.util.UUID

/**
 * Confirmed working : 7/18/21
 *
 * This class exists only to hold Key data, and register the key on load.
 */
class Data(private val container: PluginContainer) {

    companion object DataKey {
        var identityDataKey: Key<Value<UUID>>? = null
    }

    @Listener
    fun onRegisterData(event: RegisterDataEvent) {
        identityDataKey = Key.from(container, "identitydata", UUID::class.java)

        logger<Data>().info(
            """
            =
            =============================================================
            Key         :   $identityDataKey
            Plugin   :   ${container.metadata().name()}
            =============================================================
            
        """.trimIndent()
        )

        event.register(
            DataRegistration.of(
                identityDataKey,
                ItemStack::class.java
            )
        )
    }
}