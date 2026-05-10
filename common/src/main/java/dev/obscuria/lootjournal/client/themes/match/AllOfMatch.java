package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;

import java.util.List;

public record AllOfMatch(List<PickupMatch> terms) implements PickupMatch {

    public static final String NAME = "all_of";
    public static final MapCodec<AllOfMatch> CODEC;

    @Override
    public MapCodec<AllOfMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        for (var term : terms) {
            if (term.matches(pickupEvent)) continue;
            return false;
        }
        return true;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                PickupMatch.CODEC.listOf().fieldOf(NAME).forGetter(AllOfMatch::terms)
        ).apply(codec, AllOfMatch::new));
    }
}
