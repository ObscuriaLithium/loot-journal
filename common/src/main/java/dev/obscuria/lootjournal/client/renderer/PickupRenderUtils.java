package dev.obscuria.lootjournal.client.renderer;

import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.Nullable;

public final class PickupRenderUtils {

    public static final int PICKUP_HEIGHT = 14;

    public static void render(GuiGraphicsExtractor extractor, PickupRenderer renderer) {

        renderer.pushModulate(renderer.progress());

        extractor.pose().pushMatrix();
        extractor.pose().translate((float) renderer.originOffset(), 0);
        renderer.style().panel().render(extractor, renderer);
        extractor.pose().translate(renderer.paddingEdge(), renderer.paddingTop());
        @Nullable var iconPosition = renderer.layout().findFirst("ICON");
        if (iconPosition != null) {
            extractor.pose().pushMatrix();
            extractor.pose().translate((float) iconPosition.centerX(), PICKUP_HEIGHT * 0.5f);
            renderer.style().banner().render(extractor, renderer);
            extractor.pose().popMatrix();
        }
        ConfigCache.layout.render(extractor, renderer);
        extractor.pose().popMatrix();

        renderer.popModulate();
    }
}
