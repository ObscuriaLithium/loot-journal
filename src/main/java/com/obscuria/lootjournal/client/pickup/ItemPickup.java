package com.obscuria.lootjournal.client.pickup;

import com.mojang.blaze3d.vertex.PoseStack;
import com.obscuria.lootjournal.LootJournalConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
                && stack.sameItem(other.stack)
                && stack.areShareTagsEqual(other.stack)) {
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
        return Component.literal(name).withStyle(stack.getRarity().getStyleModifier());
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
    public void renderIcon(PoseStack pose, long time) {
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(pose, stack, -8, -8);
    }

    private void countTotal(int origin) {
        total = origin;
        if (Minecraft.getInstance().player == null) return;
        for (ItemStack invStack : Minecraft.getInstance().player.getInventory().items)
            total += searchSameItems(invStack);
    }

    private int searchSameItems(ItemStack stack) {
        int searched = 0;
        if (this.stack.sameItem(stack) && this.stack.areShareTagsEqual(stack))
            searched += stack.getCount();
        //Shulker Box
        if (stack.getItem() == Items.SHULKER_BOX) {
            final ItemStack shulker = stack.copy();
            final Tag tag = shulker.getOrCreateTag().getCompound("BlockEntityTag").get("Items");
            if (tag instanceof ListTag listTag)
                for (Tag tagIn : listTag.stream().toList())
                    if (tagIn instanceof CompoundTag compoundTag)
                        searched += searchSameItems(ItemStack.of(compoundTag));
        }
        //Items Tag
        if (stack.copy().getOrCreateTag().contains("Items")) {
            final ItemStack container = stack.copy();
            final Tag tag = container.getOrCreateTag().get("Items");
            if (tag instanceof ListTag listTag)
                for (Tag tagIn : listTag.stream().toList())
                    if (tagIn instanceof CompoundTag compoundTag)
                        searched += searchSameItems(ItemStack.of(compoundTag));
        }
        return searched;
    }
}
