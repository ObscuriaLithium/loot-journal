package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.events.XpPickupEvent;

public record IsXPMatch(boolean value) implements PickupMatch {

    public static final String NAME = "is_xp";
    public static final MapCodec<IsXPMatch> CODEC;

    @Override
    public MapCodec<IsXPMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        return pickupEvent instanceof XpPickupEvent;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.BOOL.fieldOf(NAME).forGetter(IsXPMatch::value)
        ).apply(codec, IsXPMatch::new));
    }
}
