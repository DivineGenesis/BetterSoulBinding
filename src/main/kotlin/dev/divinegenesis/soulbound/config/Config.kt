package dev.divinegenesis.soulbound.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting


@ConfigSerializable
class Config {
    @Setting("Modules")
    val modules : Modules = Modules()
}

@ConfigSerializable
class Modules {
    @Setting("Interact-Item")
    val interactItemModule = true
    @Setting("Pickup-Item")
    val pickupItemModule = true
    @Setting("Craft-Item")
    val craftItemModule = true
}