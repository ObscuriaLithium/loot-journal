package dev.obscuria.lootjournal.client.pickup;

import dev.obscuria.lootjournal.LootJournal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.concurrent.atomic.AtomicInteger;

public final class ItemPickup implements IPickup
{
    private final ItemStack stack;
    private int count;
    private int total;

    public ItemPickup(ItemStack stack)
    {
        this.stack = stack;
        this.count = stack.getCount();
        this.countTotal(count);
    }

    @Override
    public void renderIcon(GuiGraphics graphics, long time)
    {
        graphics.renderFakeItem(stack, -8, -8);
    }

    @Override
    public boolean tryMerge(IPickup pickup)
    {
        if (!(pickup instanceof ItemPickup other)) return false;
        if (!ItemStack.isSameItemSameComponents(stack, other.stack)) return false;
        this.count += other.count;
        this.countTotal(other.count);
        return true;
    }

    @Override
    public MutableComponent getDisplayName()
    {
        final var result = count <= 1
                ? Component.translatable("pickup.loot_journal.item_single", stack.getHoverName().getString())
                : Component.translatable("pickup.loot_journal.item_multiple", stack.getHoverName().getString(), count);
        return LootJournal.CONFIG.useRarityColor
                ? result.withStyle(stack.getRarity().color())
                : result.withColor(LootJournal.CONFIG.itemsColor);
    }

    @Override
    public boolean shouldDisplayTotal()
    {
        return LootJournal.CONFIG.displayTotal && total > 1;
    }

    @Override
    public int getTotal()
    {
        return this.total;
    }

    private void countTotal(int origin)
    {
        this.total = origin;
        final var player = Minecraft.getInstance().player;
        if (player == null) return;
        for (var stack : player.getInventory().items)
            this.total += countSameItems(stack);
    }

    private int countSameItems(ItemStack stack)
    {
        final var counter = new AtomicInteger(0);
        if (ItemStack.isSameItemSameComponents(this.stack, stack))
            counter.addAndGet(stack.getCount());
        stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).stream()
                .forEach(inner -> counter.addAndGet(countSameItems(inner)));
        return counter.get();
    }
}