package com.DivineGenesis.soulbound.data.identity;

import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.UUID;

public interface ImmutableIdentityData extends ImmutableDataManipulator<ImmutableIdentityData,IdentityData> {

    ImmutableValue<UUID> identity();

}
