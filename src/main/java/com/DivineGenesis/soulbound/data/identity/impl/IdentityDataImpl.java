package com.DivineGenesis.soulbound.data.identity.impl;

import com.DivineGenesis.soulbound.Main;
import com.DivineGenesis.soulbound.data.identity.ImmutableIdentityData;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractListData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.immutable.ImmutableBoundedValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;
import com.DivineGenesis.soulbound.data.identity.IdentityData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IdentityDataImpl extends AbstractData<IdentityData,ImmutableIdentityData> implements IdentityData {

    private UUID identity;


    public IdentityDataImpl (UUID identity) {
        this.identity = identity;

        this.registerGettersAndSetters();
    }

    public IdentityDataImpl() {
        this(null);
    }

    @Override
    public Value<UUID> identity() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(Main.IDENTITY, this.identity);
    }


    private UUID getIdentity() {
        return this.identity;
    }

    private void setIdentity(@Nullable UUID identity) {
        this.identity = identity;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerKeyValue(Main.IDENTITY, this::identity);

        registerFieldGetter(Main.IDENTITY,this::getIdentity);

        registerFieldSetter(Main.IDENTITY,this::setIdentity);
    }

    @Override
    public int getContentVersion() {
        return IdentityDataBuilder.CONTENT_VERSION;
    }

    @Override
    public ImmutableIdentityData asImmutable() {
        return new ImmutableIdentityDataImpl(this.identity);
    }

    @Override
    public Optional<IdentityData> fill(DataHolder dataHolder, MergeFunction overlap) {
        IdentityData merged = overlap.merge(this, dataHolder.get(IdentityData.class).orElse(null));
        this.identity = merged.identity().get();

        return Optional.of(this);
    }

    @Override
    public Optional<IdentityData> from(DataContainer container) {
        if (!container.contains(Main.IDENTITY)) {
            return Optional.empty();
        }
        this.identity = container.getObject(Main.IDENTITY.getQuery(),UUID.class).get();

        return Optional.of(this);

    }

    @Override
    public IdentityData copy() {
        return new IdentityDataImpl(this.identity);
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        if (this.identity != null) {
            container.set(Main.IDENTITY,this.identity);
        }

        return container;
    }

}
