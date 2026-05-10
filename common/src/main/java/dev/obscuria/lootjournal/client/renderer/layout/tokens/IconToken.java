package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderUtils;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphics;

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
    public void render(GuiGraphics graphics, PickupRenderer renderer, int x) {

        float scale = 1f + renderer.pulse() * 0.2f;
        int padding = renderer.isMirrored()
                ? renderer.style().icon().paddingRight().get()
                : renderer.style().icon().paddingLeft().get();
        float pivotX = x + padding + ICON_SIZE * 0.5f;
        float pivotY = PickupRenderUtils.PICKUP_HEIGHT * 0.5f;

        graphics.pose().pushMatrix();
        graphics.pose().translate(pivotX, pivotY);
        graphics.pose().translate(0, -4 * renderer.pulse());
        graphics.pose().scale(scale, scale);
        renderer.style().icon().render(graphics, renderer);
        graphics.pose().popMatrix();
    }

    @Override
    public String id() {
        return "ICON";
    }
}
