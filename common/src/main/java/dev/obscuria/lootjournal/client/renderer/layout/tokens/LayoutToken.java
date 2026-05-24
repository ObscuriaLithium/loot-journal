package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface LayoutToken {

    int measureWidth(PickupEvent event, PickupStyle style);

    void render(GuiGraphicsExtractor extractor, PickupRenderer renderer, int x);

    String id();
}
