package com.obscuria.lootjournal.client.renderer;

import com.mojang.blaze3d.platform.Window;
import com.obscuria.lootjournal.LootJournalConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiFunction;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public enum Anchor {
    TOP_LEFT(
            window -> 0,
            (window, offset) -> offset,
            separation -> 12 + separation,
            false),
    TOP_RIGHT(
            Window::getGuiScaledWidth,
            (window, offset) -> offset,
            separation -> 12 + separation,
            true),
    BOTTOM_LEFT(
            window -> 0,
            (window, offset) -> window.getGuiScaledHeight() - offset - 12,
            separation -> -12 - separation,
            false),
    BOTTOM_RIGHT(
            Window::getGuiScaledWidth,
            (window, offset) -> window.getGuiScaledHeight() - offset - 12,
            separation -> -12 - separation,
            true);

    private final Function<Window, Integer> originXFunc;
    private final BiFunction<Window, Integer, Integer> originYFunc;
    private final Function<Integer, Integer> stepFunc;
    private final boolean inverted;

    Anchor(Function<Window, Integer> originXFunc,
           BiFunction<Window, Integer, Integer> originYFunc,
           Function<Integer, Integer> stepFunc,
           boolean inverted) {
        this.originXFunc = originXFunc;
        this.originYFunc = originYFunc;
        this.stepFunc = stepFunc;
        this.inverted = inverted;
    }

    public int originX(Window window) {
        return originXFunc.apply(window);
    }

    public int originY(Window window) {
        return originYFunc.apply(window, LootJournalConfig.offset.get());
    }

    public int step() {
        return stepFunc.apply(LootJournalConfig.separation.get());
    }

    public boolean isInverted() {
        return inverted;
    }
}
