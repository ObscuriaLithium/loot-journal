package dev.obscuria.lootjournal.client.renderer;

import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

public final class PickupRenderUtils {

    public static final int PICKUP_HEIGHT = 14;

    public static void render(GuiGraphics graphics, PickupRenderer renderer) {

        renderer.pushModulate(renderer.progress());

        graphics.pose().pushMatrix();
        graphics.pose().translate((float) renderer.originOffset(), 0);
        renderer.style().panel().render(graphics, renderer);
        graphics.pose().translate(renderer.paddingEdge(), renderer.paddingTop());
        @Nullable var iconPosition = renderer.layout().findFirst("ICON");
        if (iconPosition != null) {
            graphics.pose().pushMatrix();
            graphics.pose().translate((float) iconPosition.centerX(), PICKUP_HEIGHT * 0.5f);
            renderer.style().banner().render(graphics, renderer);
            graphics.pose().popMatrix();
        }
        ConfigCache.layout.render(graphics, renderer);
        graphics.pose().popMatrix();

        renderer.popModulate();
    }
}
