package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public record NameToken() implements LayoutToken {

    public static final NameToken SHARED = new NameToken();

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        return Minecraft.getInstance().font.width(event.displayName());
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer renderer, int x) {
        var color = renderer.style().text().nameColor().get().rgb();
        var text = Component.empty()
                .append(renderer.event().displayName())
                .withStyle(Style.EMPTY.withColor(color));
        extractor.text(
                Minecraft.getInstance().font,
                text, x, 3, 0xFFFFFFFF,
                renderer.style().text().dropShadow().get());
    }

    @Override
    public String id() {
        return "NAME";
    }
}
