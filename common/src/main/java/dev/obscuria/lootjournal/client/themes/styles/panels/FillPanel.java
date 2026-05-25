package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.client.gui.GuiGraphics;

public record FillPanel(
        Var<ARGB> color
) implements PickupPanel {

    public static final MapCodec<FillPanel> CODEC;

    @Override
    public MapCodec<FillPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
        graphics.fill(0, 0, pickup.width(), pickup.height(), color.get().decimal());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Var.ARGB.fieldOf("color").forGetter(FillPanel::color)
        ).apply(codec, FillPanel::new));
    }
}
