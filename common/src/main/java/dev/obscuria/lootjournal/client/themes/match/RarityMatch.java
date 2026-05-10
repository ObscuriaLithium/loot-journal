package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record RarityMatch(String value) implements ItemStackMatch {

    public static final String NAME = "rarity";
    public static final MapCodec<RarityMatch> CODEC;

    @Override
    public MapCodec<RarityMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getRarity().name().equalsIgnoreCase(value);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.STRING.fieldOf(NAME).forGetter(RarityMatch::value)
        ).apply(codec, RarityMatch::new));
    }
}
