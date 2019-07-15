package com.DivineGenesis.soulbound.data.identity.impl;

import com.DivineGenesis.soulbound.Main;
import com.DivineGenesis.soulbound.data.identity.IdentityData;
import com.DivineGenesis.soulbound.data.identity.ImmutableIdentityData;
import com.DivineGenesis.soulbound.data.identity.Keys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImmutableIdentityDataImpl extends AbstractImmutableData<ImmutableIdentityData, IdentityData> implements ImmutableIdentityData {

    private final UUID identity;
    private final ImmutableValue<UUID> identityValue;


    public ImmutableIdentityDataImpl () {

        this(null);
    }

    public ImmutableIdentityDataImpl (UUID identity) {

        this.identity = checkNotNull(identity);

        this.identityValue = Sponge.getRegistry().getValueFactory().createValue(Keys.IDENTITY,identity).asImmutable();

        this.registerGetters();
    }

    @Override
    public ImmutableValue<UUID> identity () {

        return this.identityValue;
    }

    private UUID getIdentity () {

        return this.identity;
    }

    @Override
    protected void registerGetters () {

        registerKeyValue(Keys.IDENTITY,this::identity);

        registerFieldGetter(Keys.IDENTITY,this::getIdentity);
    }

    @Override
    public int getContentVersion () {

        return IdentityDataBuilder.CONTENT_VERSION;
    }

    @Override
    public IdentityDataImpl asMutable () {

        return new IdentityDataImpl(this.identity);
    }

    @Override
    public DataContainer toContainer () {

        DataContainer container = super.toContainer();

        if (this.identity != null) {
            container.set(Keys.IDENTITY,this.identity);
        }

        return container;
    }

}
