package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record ModMatch(String value) implements ItemStackMatch {

    public static final String NAME = "mod";
    public static final MapCodec<ModMatch> CODEC;

    @Override
    public MapCodec<ModMatch> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean matches(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().location().getNamespace().equals(value);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Codec.STRING.fieldOf(NAME).forGetter(ModMatch::value)
        ).apply(codec, ModMatch::new));
    }
}
