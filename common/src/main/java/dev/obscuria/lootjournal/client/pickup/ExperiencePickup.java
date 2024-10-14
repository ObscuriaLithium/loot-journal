package dev.obscuria.lootjournal.client.pickup;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.lootjournal.LootJournal;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public final class ExperiencePickup implements IPickup
{
    private static final ResourceLocation ICON = LootJournal.key("textures/gui/experience.png");
    private static final long startTime = Util.getMillis();
    private int amount;

    public ExperiencePickup(int amount)
    {
        this.amount = amount;
    }

    @Override
    public void renderIcon(GuiGraphics graphics, long time)
    {
        final var timer = (Util.getMillis() - startTime) * 0.005f;
        graphics.blit(ICON, -8, -8, 0, 0, 16, 16, 16, 32);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 0.5f + 0.5f * (float) Math.cos(timer));
        graphics.blit(ICON, -8, -8, 0, 16, 16, 16, 16, 32);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean tryMerge(IPickup pickup)
    {
        if (!(pickup instanceof ExperiencePickup other)) return false;
        this.amount += other.amount;
        return true;
    }

    @Override
    public MutableComponent getDisplayName()
    {
        return (amount <= 1
                ? Component.translatable("pickup.loot_journal.experience_single")
                : Component.translatable("pickup.loot_journal.experience_multiple", amount))
                .withStyle(Style.EMPTY.withColor(LootJournal.CONFIG.experienceColor));
    }

    @Override
    public boolean shouldDisplayTotal()
    {
        return false;
    }

    @Override
    public int getTotal()
    {
        return 0;
    }
}
