package com.obscuria.lootjournal.client.pickup;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class Pickup {

    public abstract boolean merge(Pickup other);

    public abstract MutableComponent getName();

    public abstract boolean shouldRenderTotal();

    public abstract int getTotal();

    public abstract void renderIcon(PoseStack pose, long time);
}
