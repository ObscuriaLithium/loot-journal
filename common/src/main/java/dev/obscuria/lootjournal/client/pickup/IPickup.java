package dev.obscuria.lootjournal.client.pickup;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

public interface IPickup
{
    void renderIcon(GuiGraphics graphics, long time);

    boolean tryMerge(IPickup pickup);

    MutableComponent getDisplayName();

    boolean shouldDisplayTotal();

    int getTotal();
}