package com.divinegenesis.soulbound.data;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableSoulboundData extends AbstractImmutableData<ImmutableSoulboundData, SoulboundData>
{
	private UUID user;
	
	public ImmutableSoulboundData()
	{
		this(UUID.fromString(""));
	}
	
	public ImmutableSoulboundData(UUID uuid)
	{
		this.user = uuid;
	}

	public ImmutableValue<UUID> userUUID()
	{
		return Sponge.getRegistry().getValueFactory().createValue(SoulboundKeys.SOULBOUND_USER, this.user).asImmutable();
	}
	
	@Override
	public SoulboundData asMutable() 
	{
		return new SoulboundData();
	}

	@Override
	protected void registerGetters() 
	{
		registerFieldGetter(SoulboundKeys.SOULBOUND_USER, this::getUser);
		registerKeyValue(SoulboundKeys.SOULBOUND_USER, this::userUUID);
	} 
	
	private UUID getUser()
	{
		return this.user;
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
