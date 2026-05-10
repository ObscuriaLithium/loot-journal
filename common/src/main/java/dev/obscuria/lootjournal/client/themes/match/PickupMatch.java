package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.lootjournal.LootJournalCodecs;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.registry.CompactDispatchCodec;

import java.util.function.Function;

public interface PickupMatch {

    Codec<PickupMatch> CODEC = CompactDispatchCodec.create(
            LootJournalCodecs.byNameCodec(LootJournalRegistries.PICKUP_MATCH_TYPE),
            PickupMatch::codec, Function.identity());

    MapCodec<? extends PickupMatch> codec();

    boolean matches(PickupEvent pickupEvent);

    static void bootstrap(BootstrapContext<MapCodec<? extends PickupMatch>> context) {
        context.register(AlwaysMatch.NAME, () -> AlwaysMatch.CODEC);
        context.register(AllOfMatch.NAME, () -> AllOfMatch.CODEC);
        context.register(AnyOfMatch.NAME, () -> AnyOfMatch.CODEC);
        context.register(NoneOfMatch.NAME, () -> NoneOfMatch.CODEC);
        context.register(IsItemMatch.NAME, () -> IsItemMatch.CODEC);
        context.register(IsXPMatch.NAME, () -> IsXPMatch.CODEC);
        context.register(ItemMatch.NAME, () -> ItemMatch.CODEC);
        context.register(ItemTagMatch.NAME, () -> ItemTagMatch.CODEC);
        context.register(ModMatch.NAME, () -> ModMatch.CODEC);
        context.register(RarityMatch.NAME, () -> RarityMatch.CODEC);
    }
}
