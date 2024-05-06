package com.obscuria.lootjournal.client.pickup;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MoreItemsPickup extends Pickup {
    protected List<ItemStack> stacks = new ArrayList<>();
    protected int count;

    public static MoreItemsPickup of(ItemStack stack) {
        return new MoreItemsPickup(stack);
    }

    protected MoreItemsPickup(ItemStack stack) {
        this.stacks.add(stack);
        this.count = stack.getCount();
    }

    @Override
    public boolean merge(Pickup pickup) {
        if (pickup instanceof MoreItemsPickup other) {
            stacks.addAll(other.stacks);
            count += other.count;
            return true;
        }
        return false;
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable("pickup.loot_journal.other_items", count)
                .withStyle(ChatFormatting.WHITE);
    }

    @Override
    public boolean shouldRenderTotal() {
        return false;
    }

    @Override
    public int getTotal() {
        return 0;
    }

    @Override
    public void renderIcon(PoseStack pose, long time) {
        if (stacks.isEmpty()) return;
        final var interval = Math.max(200, 1000 - 50 * stacks.size());
        final var stack = stacks.get((int) (time / interval % stacks.size()));
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(pose, stack, -8, -8);
    }
}
