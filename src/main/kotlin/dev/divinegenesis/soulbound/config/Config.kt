package dev.divinegenesis.soulbound.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
class Config {
    @Setting("Modules")
    val modules: Modules = Modules()
}

@ConfigSerializable
class Modules {
    @Setting("Interact-Item")
    val interactItemModule = true

    @Setting("Pickup-Item")
    val pickupItemModule = true

    @Setting("Craft-Item")
    val craftItemModule = true

    @Setting("Item-DeSpawn")
    @Comment("Prevents bound items from de-spawning due to Minecrafts natural clear cycle")
    val itemDeSpawnModule = true

    @Setting("OnDeath-Return")
    @Comment("Returns bound items upon players death")
    val onDeathModule = true
}