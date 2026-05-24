package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderUtils;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public record IconToken() implements LayoutToken {

    public static final IconToken SHARED = new IconToken();
    private static final int ICON_SIZE = 16;

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        return ICON_SIZE
                + style.icon().paddingLeft().get()
                + style.icon().paddingRight().get();
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer renderer, int x) {

        float scale = 1f + renderer.pulse() * 0.2f;
        int padding = renderer.isMirrored()
                ? renderer.style().icon().paddingRight().get()
                : renderer.style().icon().paddingLeft().get();
        float pivotX = x + padding + ICON_SIZE * 0.5f;
        float pivotY = PickupRenderUtils.PICKUP_HEIGHT * 0.5f;

        extractor.pose().pushMatrix();
        extractor.pose().translate(pivotX, pivotY);
        extractor.pose().translate(0, -4 * renderer.pulse());
        extractor.pose().scale(scale, scale);
        renderer.style().icon().render(extractor, renderer);
        extractor.pose().popMatrix();
    }

    @Override
    public String id() {
        return "ICON";
    }
}
