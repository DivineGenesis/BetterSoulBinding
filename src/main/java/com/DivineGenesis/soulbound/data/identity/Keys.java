package com.DivineGenesis.soulbound.data.identity;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import java.util.UUID;

public class Keys {

    public static Key<Value<UUID>> IDENTITY = DummyObjectProvider.createExtendedFor(Key.class, "IDENTITY");

    public Keys () {
        IDENTITY = Key.builder()
                .type(new TypeToken<Value<UUID>>() {})
                .id("identity")
                .name("identity")
                .query(DataQuery.of("identity"))
                .build();
    }

}
