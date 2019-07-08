package com.DivineGenesis.soulbound.data.identity;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.mutable.ListData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.UUID;

public interface IdentityData extends DataManipulator<IdentityData, ImmutableIdentityData> {

    Value<UUID> identity();

}

