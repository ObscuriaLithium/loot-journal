package dev.obscuria.lootjournal.client.pickup;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

public interface IPickupEntry
{
    MutableComponent getDisplayName();

    int getTotalAmount();

    void renderIcon(GuiGraphics graphics, long time);

    boolean maybeMerge(IPickupEntry pickup);

    boolean shouldDisplayTotalAmount();
}