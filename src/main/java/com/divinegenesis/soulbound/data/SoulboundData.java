package com.divinegenesis.soulbound.data;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

public class SoulboundData extends AbstractData<SoulboundData, ImmutableSoulboundData>
{
	private UUID user;
	
	public SoulboundData() 
	{
		this(UUID.fromString(""));
	}
	
	public SoulboundData(UUID uuid)
	{
		user = uuid;
		registerGettersAndSetters();
	}
	
	public Value<UUID> soulboundUser()
	{
		return Sponge.getRegistry().getValueFactory().createValue(SoulboundKeys.SOULBOUND_USER, user);
	}
	
	private UUID getUser()
	{
		return user;
	}
	
	private void setUser(UUID uuid)
	{
		this.user = uuid;
	}

	@Override
	protected void registerGettersAndSetters() 
	{
		registerFieldGetter(SoulboundKeys.SOULBOUND_USER, () -> this.user);
		registerFieldSetter(SoulboundKeys.SOULBOUND_USER, value -> this.user = value);
		registerKeyValue(SoulboundKeys.SOULBOUND_USER, this::soulboundUser);
	}
	
	@Override
    public Optional<SoulboundData> fill(DataHolder dataHolder) 
	{
        SoulboundData data = dataHolder.get(SoulboundData.class).orElse(null);
        return Optional.ofNullable(data);
    }
	
	@Override
	public Optional<SoulboundData> fill(DataHolder dataHolder, MergeFunction overlap) 
	{
		 SoulboundData data = overlap.merge(this, dataHolder.get(SoulboundData.class).orElse(null));
	     setUser(data.getUser());
	     return Optional.of(this);
	}

	@Override
	public Optional<SoulboundData> from(DataContainer container) 
	{
		if(container.contains(SoulboundKeys.SOULBOUND_USER.getQuery()))
		{
			final UUID uuid = (UUID) container.get(SoulboundKeys.SOULBOUND_USER.getQuery()).get();
			setUser(uuid);
		}
		return Optional.of(this);
	}

	@Override
	public SoulboundData copy() 
	{
		return new SoulboundData(this.getUser());
	}

	@Override
	public ImmutableSoulboundData asImmutable() 
	{
		return new ImmutableSoulboundData(this.getUser());
	}
	
	@Override
	public int getContentVersion()
	{
		return 1;
	}
	
	@Override
	public DataContainer toContainer()
	{
		return super.toContainer()
				.set(SoulboundKeys.SOULBOUND_USER, this.getUser());
	}
}
