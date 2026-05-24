package dev.obscuria.lootjournal.client.themes.styles.icons;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.IconEffect;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.NoneEffect;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public record SimpleIcon(
        Var<Integer> xOffset,
        Var<Integer> yOffset,
        Var<Integer> paddingLeft,
        Var<Integer> paddingRight,
        IconEffect effect
) implements PickupIcon {

    public static final MapCodec<SimpleIcon> CODEC;
    public static final SimpleIcon DEFAULT;

    @Override
    public MapCodec<SimpleIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer renderer) {
        effect.render(extractor, renderer);
        renderer.event().renderIcon(extractor, renderer);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Var.INT.fieldOf("x_offset").forGetter(SimpleIcon::xOffset),
                Var.INT.fieldOf("y_offset").forGetter(SimpleIcon::yOffset),
                Var.INT.fieldOf("padding_left").forGetter(SimpleIcon::paddingLeft),
                Var.INT.fieldOf("padding_right").forGetter(SimpleIcon::paddingRight),
                IconEffect.CODEC.optionalFieldOf("effect", NoneEffect.SHARED).forGetter(SimpleIcon::effect)
        ).apply(codec, SimpleIcon::new));
        var zero = new Var.DirectVar<>(0);
        DEFAULT = new SimpleIcon(zero, zero, zero, zero, NoneEffect.SHARED);
    }
}

