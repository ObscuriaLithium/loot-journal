package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.v2.api.common.Color;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public record FillPanel(
        Var<Color> color
) implements PickupPanel {

    public static final MapCodec<FillPanel> CODEC;

    @Override
    public MapCodec<FillPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer pickup) {
        extractor.fill(0, 0, pickup.width(), pickup.height(), color.get().argb());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Var.COLOR.fieldOf("color").forGetter(FillPanel::color)
        ).apply(codec, FillPanel::new));
    }
}
