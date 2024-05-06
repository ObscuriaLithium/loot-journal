package com.obscuria.lootjournal.client.pickup;

import com.obscuria.lootjournal.LootJournalConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.atomic.AtomicInteger;

@OnlyIn(Dist.CLIENT)
public class ItemPickup extends Pickup {
    protected final ItemStack stack;
    protected int count, total;

    public static ItemPickup of(ItemStack stack) {
        return new ItemPickup(stack);
    }

    protected ItemPickup(ItemStack stack) {
        this.stack = stack;
        this.count = stack.getCount();
        countTotal(count);
    }

    @Override
    public boolean merge(Pickup pickup) {
        if (pickup instanceof ItemPickup other
                && ItemStack.isSameItemSameComponents(stack, other.stack)) {
            count += other.count;
            countTotal(other.count);
            return true;
        }
        return false;
    }

    @Override
    public MutableComponent getName() {
        var name = stack.getHoverName().getString();
        if (name.length() > 24) name = name.substring(0, 23) + "...";
        if (count > 1) name += " x" + count;
        return Component.literal(name).withStyle(stack.getRarity().color());
    }

    @Override
    public boolean shouldRenderTotal() {
        return LootJournalConfig.total.get() && total > 1;
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    public void renderIcon(GuiGraphics graphics, long time) {
        graphics.renderFakeItem(stack, -8, -8);
    }

    private void countTotal(int origin) {
        total = origin;
        if (Minecraft.getInstance().player == null) return;
        for (ItemStack invStack : Minecraft.getInstance().player.getInventory().items)
            total += searchSameItems(invStack);
    }

    private int searchSameItems(ItemStack stack) {
        final var searched = new AtomicInteger(0);
        if (ItemStack.isSameItemSameComponents(this.stack, stack))
            searched.addAndGet(stack.getCount());
        stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).stream()
                .forEach(inner -> searched.addAndGet(searchSameItems(inner)));
        return searched.get();
    }
}
