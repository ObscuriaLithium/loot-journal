package dev.obscuria.lootjournal.client.render;

import com.mojang.blaze3d.platform.Window;
import dev.obscuria.lootjournal.LootJournal;

import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum Anchor
{
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
    private final boolean reversed;

    Anchor(Function<Window, Integer> originXFunc,
           BiFunction<Window, Integer, Integer> originYFunc,
           Function<Integer, Integer> stepFunc,
           boolean reversed)
    {
        this.originXFunc = originXFunc;
        this.originYFunc = originYFunc;
        this.stepFunc = stepFunc;
        this.reversed = reversed;
    }

    public int originX(Window window)
    {
        return originXFunc.apply(window);
    }

    public int originY(Window window)
    {
        return originYFunc.apply(window, LootJournal.CONFIG.anchorOffset);
    }

    public int step()
    {
        return stepFunc.apply(LootJournal.CONFIG.notificationSeparation);
    }

    public boolean isReversed()
    {
        return reversed;
    }
}
