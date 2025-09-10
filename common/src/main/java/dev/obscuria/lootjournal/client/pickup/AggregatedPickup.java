package dev.obscuria.lootjournal.client.pickup;

import dev.obscuria.lootjournal.LootJournal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public final class AggregatedPickup implements IPickupEntry
{
    private final List<ItemStack> stacks = Lists.newArrayList();
    private int count;

    public AggregatedPickup(ItemStack stack)
    {
        this.stacks.add(stack);
        this.count = stack.getCount();
    }

    @Override
    public MutableComponent getDisplayName()
    {
        return (count <= 1
                ? Component.translatable("pickup.loot_journal.grouped_items_single")
                : Component.translatable("pickup.loot_journal.grouped_items_multiple", count))
                .withStyle(LootJournal.CONFIG.aggregatedEntryStyle);
    }

    @Override
    public int getTotalAmount()
    {
        return 0;
    }

    @Override
    public void renderIcon(GuiGraphics graphics, long time)
    {
        if (stacks.isEmpty()) return;
        final var interval = Math.max(200, 1000 - 50 * stacks.size());
        final var stack = stacks.get((int) (time / interval % stacks.size()));
        graphics.renderFakeItem(stack, -8, -8);
    }

    @Override
    public boolean maybeMerge(IPickupEntry pickup)
    {
        if (!(pickup instanceof AggregatedPickup other)) return false;
        this.stacks.addAll(other.stacks);
        this.count += other.count;
        return true;
    }

    @Override
    public boolean shouldDisplayTotalAmount()
    {
        return false;
    }
}