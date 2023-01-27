package com.obscuria.lootjournal;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class Pickup {
    private static final float MAX_OFFSET = 180f;
    public ItemStack stack;
    public int count;
    public int lifetime = 0;
    public int total = 0;

    public float offset = MAX_OFFSET;
    public float offsetLerp = offset;
    public Pickup(ItemStack stack) {
        this.stack = stack;
        this.count = stack.getCount();
        this.setTotal(stack.getCount());
    }
    public void tick(List<Pickup> list) {
        final int maxLifetime = LootJournalConfig.Client.pickupLifetime.get();
        this.lifetime++;
        offsetLerp = offset;
        offset = Math.max(0, Math.min(MAX_OFFSET, lifetime < maxLifetime * 20 ? offset - 15 : offset + 5));
        if (lifetime > maxLifetime * 20 + 30)
            list.remove(this);
    }

    public void setTotal(int count) {
        if (Minecraft.getInstance().player == null) return;
        for (ItemStack invStack : Minecraft.getInstance().player.getInventory().items)
            if (invStack.sameItem(stack)) count += invStack.getCount();
        this.total = count;
    }
}
