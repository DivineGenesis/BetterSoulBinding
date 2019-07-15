package com.DivineGenesis.soulbound.data.identity.impl;

import com.DivineGenesis.soulbound.Main;
import com.DivineGenesis.soulbound.data.identity.IdentityData;
import com.DivineGenesis.soulbound.data.identity.ImmutableIdentityData;
import com.DivineGenesis.soulbound.data.identity.Keys;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;
import java.util.UUID;

public class IdentityDataBuilder extends AbstractDataBuilder<IdentityData> implements DataManipulatorBuilder<IdentityData, ImmutableIdentityData> {

    public static final int CONTENT_VERSION = 1;

    public IdentityDataBuilder () {

        super(IdentityData.class,CONTENT_VERSION);
    }

    @Override
    public IdentityDataImpl create () {

        return new IdentityDataImpl();
    }

    @Override
    public Optional<IdentityData> createFrom (DataHolder dataHolder) {

        return create().fill(dataHolder);
    }

    @Override
    public Optional<IdentityData> buildContent (DataView container) throws InvalidDataException {

        if (!container.contains(Keys.IDENTITY)) {
            return Optional.empty();
        }

        IdentityData data = new IdentityDataImpl();

        container.getObject(Keys.IDENTITY.getQuery(),UUID.class).ifPresent(identity->data.set(Keys.IDENTITY,identity));

        return Optional.of(data);
    }
}

