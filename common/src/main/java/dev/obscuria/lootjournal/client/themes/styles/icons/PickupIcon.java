package dev.obscuria.lootjournal.client.themes.styles.icons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.v2.api.common.registry.BootstrapContext;
import dev.obscuria.lootjournal.LootJournalCodecs;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.function.Function;

public interface PickupIcon {

    Codec<PickupIcon> CODEC = LootJournalCodecs
            .byNameCodec(LootJournalRegistries.PICKUP_ICON_TYPE)
            .dispatch(PickupIcon::codec, Function.identity());

    MapCodec<? extends PickupIcon> codec();

    void render(GuiGraphicsExtractor extractor, PickupRenderer renderer);

    Var<Integer> paddingLeft();

    Var<Integer> paddingRight();

    static void bootstrap(BootstrapContext<MapCodec<? extends PickupIcon>> context) {
        context.register("simple", () -> SimpleIcon.CODEC);
    }
}
