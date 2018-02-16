package com.divinegenesis.soulbound.data;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

public class SoulboundDataBuilder extends AbstractDataBuilder<SoulboundData> implements DataManipulatorBuilder<SoulboundData, ImmutableSoulboundData>
{

	public SoulboundDataBuilder()
	{
		super(SoulboundData.class, 1);
	}

	@Override
	public SoulboundData create() 
	{
		return new SoulboundData();
	}

	@Override
	public Optional<SoulboundData> createFrom(DataHolder dataHolder)
	{
		return Optional.of(dataHolder.get(SoulboundData.class).orElse(new SoulboundData()));
	}

	@Override
	protected Optional<SoulboundData> buildContent(DataView container) throws InvalidDataException 
	{
		if( container.contains(SoulboundKeys.SOULBOUND_USER.getQuery() ))
		{
			final UUID user = (UUID) container.get(SoulboundKeys.SOULBOUND_USER.getQuery()).get();
			return Optional.of(new SoulboundData(user));
		}
			
		return Optional.empty();
	}
	
}