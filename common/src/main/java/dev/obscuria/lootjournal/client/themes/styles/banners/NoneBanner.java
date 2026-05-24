package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.MapCodec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public record NoneBanner() implements PickupBanner {

    public static final MapCodec<NoneBanner> CODEC = MapCodec.unit(NoneBanner::new);
    public static final NoneBanner DEFAULT = new NoneBanner();

    @Override
    public MapCodec<NoneBanner> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer pickup) {}
}
