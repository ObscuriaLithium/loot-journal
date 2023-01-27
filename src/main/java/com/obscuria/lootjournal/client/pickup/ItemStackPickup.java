package com.obscuria.lootjournal.client.pickup;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemStackPickup extends Pickup {
    public final ItemStack STACK;
    public int count, total;

    public ItemStackPickup(ItemStack stack) {
        this.STACK = stack;
        this.count = stack.getCount();
        this.countTotal(stack.getCount());
    }

    @Override
    public boolean merge(Pickup pickup) {
        if (pickup instanceof ItemStackPickup other && other.STACK.sameItem(STACK) && other.STACK.getHoverName().equals(STACK.getHoverName())) {
            this.count += other.STACK.getCount();
            this.lifetime = 0;
            this.countTotal(other.STACK.getCount());
            return true;
        }
        return false;
    }

    @Override
    public void countTotal(int addition) {
        if (Minecraft.getInstance().player == null) return;
        for (ItemStack invStack : Minecraft.getInstance().player.getInventory().items) addition += searchSameItems(invStack);
        this.total = addition;
    }

    private int searchSameItems(ItemStack stack) {
        int searched = 0;
        //Common
        if (stack.sameItem(STACK)) searched += stack.getCount();
        //Shulker Box
        if (stack.getItem() == Items.SHULKER_BOX) {
            final ItemStack shulker = stack.copy();
            final Tag tag = shulker.getOrCreateTag().getCompound("BlockEntityTag").get("Items");
            if (tag instanceof ListTag listTag) for (Tag tagIn : listTag.stream().sequential().toList())
                if (tagIn instanceof CompoundTag compoundTag) searched += searchSameItems(ItemStack.of(compoundTag));
        }
        //Items Tag
        if (stack.copy().getOrCreateTag().contains("Items")) {
            final ItemStack container = stack.copy();
            final Tag tag = container.getOrCreateTag().get("Items");
            if (tag instanceof ListTag listTag) for (Tag tagIn : listTag.stream().sequential().toList())
                if (tagIn instanceof CompoundTag compoundTag) searched += searchSameItems(ItemStack.of(compoundTag));
        }
        return searched;
    }
}
