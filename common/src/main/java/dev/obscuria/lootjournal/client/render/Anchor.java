package dev.obscuria.lootjournal.client.render;

import com.mojang.blaze3d.platform.Window;
import dev.obscuria.lootjournal.LootJournal;
import net.minecraft.client.gui.GuiGraphics;

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
    private final Function<Window, Integer> xFunc;
    private final BiFunction<Window, Integer, Integer> yFunc;
    private final Function<Integer, Integer> stepFunc;
    private final boolean reversed;

    Anchor(Function<Window, Integer> xFunc,
           BiFunction<Window, Integer, Integer> yFunc,
           Function<Integer, Integer> stepFunc,
           boolean reversed)
    {
        this.xFunc = xFunc;
        this.yFunc = yFunc;
        this.stepFunc = stepFunc;
        this.reversed = reversed;
    }

    public int getX(Window window)
    {
        return xFunc.apply(window);
    }

    public int getY(Window window)
    {
        var offset = window.getGuiScaledHeight() * (LootJournal.CONFIG.anchorPercentOffset * 0.01);
        return yFunc.apply(window, (int) offset + LootJournal.CONFIG.anchorPixelOffset);
    }

    public int getStep()
    {
        return stepFunc.apply(LootJournal.CONFIG.separation);
    }

    public boolean isReversed()
    {
        return reversed;
    }

    public void transform(GuiGraphics graphics, Window window)
    {
        graphics.pose().translate(getX(window), getY(window), 0);
        var scale = LootJournal.CONFIG.scale * 0.01f;
        graphics.pose().scale(scale, scale, scale);
    }
}
