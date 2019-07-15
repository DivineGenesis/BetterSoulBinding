package com.DivineGenesis.soulbound.data.identity.impl;

import com.DivineGenesis.soulbound.Main;
import com.DivineGenesis.soulbound.data.identity.IdentityData;
import com.DivineGenesis.soulbound.data.identity.ImmutableIdentityData;
import com.DivineGenesis.soulbound.data.identity.Keys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class IdentityDataImpl extends AbstractData<IdentityData, ImmutableIdentityData> implements IdentityData {

    private UUID identity;


    public IdentityDataImpl (UUID identity) {

        this.identity = identity;

        this.registerGettersAndSetters();
    }

    public IdentityDataImpl () {

        this(null);
    }

    @Override
    public Value<UUID> identity () {

        return Sponge.getRegistry().getValueFactory().createValue(Keys.IDENTITY,this.identity);
    }


    private UUID getIdentity () {

        return this.identity;
    }

    private void setIdentity (@Nullable UUID identity) {

        this.identity = identity;
    }

    @Override
    protected void registerGettersAndSetters () {

        registerKeyValue(Keys.IDENTITY,this::identity);

        registerFieldGetter(Keys.IDENTITY,this::getIdentity);

        registerFieldSetter(Keys.IDENTITY,this::setIdentity);
    }

    @Override
    public int getContentVersion () {

        return IdentityDataBuilder.CONTENT_VERSION;
    }

    @Override
    public ImmutableIdentityData asImmutable () {

        return new ImmutableIdentityDataImpl(this.identity);
    }

    @Override
    public Optional<IdentityData> fill (DataHolder dataHolder,MergeFunction overlap) {

        IdentityData merged = overlap.merge(this,dataHolder.get(IdentityData.class).orElse(null));
        this.identity = merged.identity().get();

        return Optional.of(this);
    }

    @Override
    public Optional<IdentityData> from (DataContainer container) {

        if (!container.contains(Keys.IDENTITY)) {
            return Optional.empty();
        }
        this.identity = container.getObject(Keys.IDENTITY.getQuery(),UUID.class).get();

        return Optional.of(this);

    }

    @Override
    public IdentityData copy () {

        return new IdentityDataImpl(this.identity);
    }

    @Override
    public DataContainer toContainer () {

        DataContainer container = super.toContainer();
        if (this.identity == null) {
            container.set(Keys.IDENTITY,this.identity);
        }

        return container;
    }

}
