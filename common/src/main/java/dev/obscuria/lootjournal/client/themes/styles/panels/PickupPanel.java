package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.v2.api.common.registry.BootstrapContext;
import dev.obscuria.lootjournal.LootJournalCodecs;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.function.Function;

public interface PickupPanel {

    Codec<PickupPanel> CODEC = LootJournalCodecs
            .byNameCodec(LootJournalRegistries.PICKUP_PANEL_TYPE)
            .dispatch(PickupPanel::codec, Function.identity());

    MapCodec<? extends PickupPanel> codec();

    void render(GuiGraphicsExtractor extractor, PickupRenderer pickup);

    static void bootstrap(BootstrapContext<MapCodec<? extends PickupPanel>> context) {
        context.register("none", () -> NonePanel.CODEC);
        context.register("fill", () -> FillPanel.CODEC);
        context.register("nine_sliced", () -> NineSlicedPanel.CODEC);
    }
}
