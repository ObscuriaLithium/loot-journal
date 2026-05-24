package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public record TotalToken() implements LayoutToken {

    public static final TotalToken SHARED = new TotalToken();

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        if (LootJournalHelper.isSelf(event.player())) {
            if (!event.supportsTotalCount()) return 0;
            return Minecraft.getInstance().font.width(format(event.total(), null));
        } else return 12;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer renderer, int x) {
        if (LootJournalHelper.isSelf(renderer.event().player())) {
            if (!renderer.event().supportsTotalCount()) return;
            extractor.text(
                    Minecraft.getInstance().font,
                    format(renderer.event().total(), renderer.style()), x, 3, 0xFFFFFFFF,
                    renderer.style().text().dropShadow().get());
        } else {
            var texture = renderer.event().player().getSkin();
            PlayerFaceExtractor.extractRenderState(extractor, texture, x, 1, 12);
        }
    }

    @Override
    public String id() {
        return "TOTAL";
    }

    private Component format(int total, @Nullable PickupStyle style) {
        var color = style == null ? 0xffffff : style.text().totalCountColor().get().rgb();
        var textStyle = Style.EMPTY.withColor(color);
        return Component.literal(LootJournal.abbreviate(total)).withStyle(textStyle);
    }
}
