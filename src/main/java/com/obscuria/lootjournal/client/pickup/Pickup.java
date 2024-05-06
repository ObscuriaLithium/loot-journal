package com.obscuria.lootjournal.client.pickup;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class Pickup {

    public abstract boolean merge(Pickup other);

    public abstract MutableComponent getName();

    public abstract boolean shouldRenderTotal();

    public abstract int getTotal();

    public abstract void renderIcon(GuiGraphics graphics, long time);
}
