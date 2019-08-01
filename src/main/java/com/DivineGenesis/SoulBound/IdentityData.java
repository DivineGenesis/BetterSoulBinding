package com.DivineGenesis.SoulBound;


import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import javax.annotation.Generated;
import java.util.Optional;
import java.util.UUID;

import static com.DivineGenesis.SoulBound.Reference.Blank_UUID;


@Generated (value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2019-07-15T21:25:00.635Z")
public class IdentityData extends AbstractData<IdentityData, IdentityData.Immutable> {

    private UUID identity;

    {
        registerGettersAndSetters();
    }

    IdentityData () {

        identity = Blank_UUID;
    }

    IdentityData (UUID identity) {

        this.identity = identity;
    }

    @Override
    protected void registerGettersAndSetters () {

        registerFieldGetter(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, this::getIdentity);
        registerFieldSetter(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, this::setIdentity);
        registerKeyValue(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, this::identity);
    }

    public UUID getIdentity () {

        return identity;
    }

    public void setIdentity (UUID identity) {

        this.identity = identity;
    }

    public Value<UUID> identity () {

        return Sponge.getRegistry().getValueFactory().createValue(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, identity);
    }

    @Override
    public Optional<IdentityData> fill (DataHolder dataHolder, MergeFunction overlap) {

        dataHolder.get(IdentityData.class).ifPresent(that->{
            IdentityData data = overlap.merge(this, that);
            this.identity = data.identity;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<IdentityData> from (DataContainer container) {

        return from((DataView) container);
    }

    public Optional<IdentityData> from (DataView container) {

        container.getObject(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY.getQuery(), UUID.class).ifPresent(v->identity = v);
        return Optional.of(this);
    }

    @Override
    public IdentityData copy () {

        return new IdentityData(identity);
    }

    @Override
    public Immutable asImmutable () {

        return new Immutable(identity);
    }

    @Override
    public int getContentVersion () {

        return 1;
    }

    @Override
    public DataContainer toContainer () {

        return super.toContainer().set(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY.getQuery(), identity);
    }

    @Generated (value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2019-07-15T21:25:00.647Z")
    public static class Immutable extends AbstractImmutableData<Immutable, IdentityData> {

        private UUID identity;

        {
            registerGetters();
        }

        Immutable () {

            identity = Blank_UUID;
        }

        Immutable (UUID identity) {

            this.identity = identity;
        }

        @Override
        protected void registerGetters () {

            registerFieldGetter(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, this::getIdentity);
            registerKeyValue(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, this::identity);
        }

        public UUID getIdentity () {

            return identity;
        }

        public ImmutableValue<UUID> identity () {

            return Sponge.getRegistry().getValueFactory().createValue(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY, identity).asImmutable();
        }

        @Override
        public IdentityData asMutable () {

            return new IdentityData(identity);
        }

        @Override
        public int getContentVersion () {

            return 1;
        }

        @Override
        public DataContainer toContainer () {

            return super.toContainer().set(com.DivineGenesis.SoulBound.IdentityKeys.IDENTITY.getQuery(), identity);
        }

    }

    @Generated (value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2019-07-15T21:25:00.649Z")
    public static class Builder extends AbstractDataBuilder<IdentityData> implements DataManipulatorBuilder<IdentityData, Immutable> {

        protected Builder () {

            super(IdentityData.class, 1);
        }

        @Override
        public IdentityData create () {

            return new IdentityData();
        }

        @Override
        public Optional<IdentityData> createFrom (DataHolder dataHolder) {

            return create().fill(dataHolder);
        }

        @Override
        protected Optional<IdentityData> buildContent (DataView container) throws InvalidDataException {

            return create().from(container);
        }

    }
}
