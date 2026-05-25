package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.MapCodec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record NonePanel() implements PickupPanel {

    public static final MapCodec<NonePanel> CODEC = MapCodec.unit(NonePanel::new);
    public static final NonePanel DEFAULT = new NonePanel();

    @Override
    public MapCodec<NonePanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {}
}
