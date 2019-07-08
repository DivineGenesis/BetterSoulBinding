package com.DivineGenesis.soulbound.data.identity.impl;

import com.DivineGenesis.soulbound.Main;
import com.DivineGenesis.soulbound.data.identity.IdentityData;
import com.DivineGenesis.soulbound.data.identity.ImmutableIdentityData;
import com.sun.javafx.geom.transform.Identity;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;
import java.util.UUID;

public class IdentityDataBuilder extends AbstractDataBuilder<IdentityData> implements DataManipulatorBuilder<IdentityData,ImmutableIdentityData> {

    public static final int CONTENT_VERSION = 1;

    public IdentityDataBuilder() {
        super(IdentityData.class, CONTENT_VERSION);
    }

    @Override
    public IdentityDataImpl create() {
        return new IdentityDataImpl();
    }

    @Override
    public Optional<IdentityData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    public Optional<IdentityData> buildContent(DataView container) throws InvalidDataException {

        if (!container.contains(Main.IDENTITY)) {
            return Optional.empty();
        }

        IdentityData data = new IdentityDataImpl();

        container.getSerializable(Main.IDENTITY.getQuery(),IdentityData.class).ifPresent(identity-> {
            data.set(Main.IDENTITY, identity);
        });

        return Optional.of(data);
    }
}

