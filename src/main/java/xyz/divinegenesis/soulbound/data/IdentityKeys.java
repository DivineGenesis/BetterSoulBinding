package xyz.divinegenesis.soulbound.data;


import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;

import javax.annotation.Generated;
import java.util.UUID;


@SuppressWarnings ("deprecation")
@Generated (value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2019-07-15T21:25:00.649Z")
public class IdentityKeys {

    protected IdentityKeys () {

    }

    public static final Key<Value<UUID>> IDENTITY;

    static {
        TypeToken<UUID> uuidToken = TypeToken.of(UUID.class);
        TypeToken<Value<UUID>> valueUUIDToken = new TypeToken<Value<UUID>>() {

        };
        IDENTITY = KeyFactory.makeSingleKey(uuidToken, valueUUIDToken, DataQuery.of("Identity"), "bettersoulbinding:identity", "Identity");
    }
}
