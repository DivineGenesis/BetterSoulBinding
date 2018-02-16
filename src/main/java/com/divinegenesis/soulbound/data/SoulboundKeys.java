package com.divinegenesis.soulbound.data;

import java.util.UUID;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import com.google.common.reflect.TypeToken;

public class SoulboundKeys 
{
    public static Key< Value<UUID> > SOULBOUND_USER;
    
    public static void init()
    {
    	SOULBOUND_USER = Key.builder()
    			.type(new TypeToken<Value<UUID>>() {})
    			.query(DataQuery.of("SoulboundPlayer"))
    			.id("soulbound:soulbound_player")
    			.name("Soulbound User UUID")
    			.build();
    }
}
