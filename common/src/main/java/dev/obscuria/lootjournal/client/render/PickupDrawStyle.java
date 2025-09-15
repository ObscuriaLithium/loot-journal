package dev.obscuria.lootjournal.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.pickup.IPickupEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public enum PickupDrawStyle
{
    CLASSIC(PickupDrawStyle::renderClassic),
    FLAT(PickupDrawStyle::renderFlat),
    TEXT(PickupDrawStyle::renderText);
    private static final ResourceLocation DECORATIONS = LootJournal.key("textures/gui/decorations.png");
    private final Renderer renderer;

    PickupDrawStyle(Renderer renderer)
    {
        this.renderer = renderer;
    }

    public void render(IPickupEntry pickup, GuiGraphics graphics, int y, double progress, long time)
    {
        final var font = Minecraft.getInstance().font;
        final var name = pickup.getDisplayName();
        
        final var total = pickup.shouldDisplayTotalAmount()
                ? Component.literal(String.valueOf(pickup.getTotalAmount()))
                : Component.empty();
        final var totalWidth = pickup.shouldDisplayTotalAmount() ? font.width(total) + 5 : 0;
        
        graphics.pose().pushPose();
        graphics.pose().translate(getX(progress), y, 100);
        RenderSystem.enableBlend();
        renderer.render(pickup, graphics, font, name, font.width(name), total, totalWidth, time);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }

    private static void renderClassic(IPickupEntry pickup, GuiGraphics graphics, Font font,
                                      MutableComponent name, int nameWidth,
                                      MutableComponent total, int totalWidth,
                                      long time)
    {
        if (isReversed())
        {
            graphics.blit(DECORATIONS, -36 - nameWidth - totalWidth, 0, 0, 0, 256, 12, 256, 256);
            graphics.blit(DECORATIONS, -26 - totalWidth, 1, 0, 12, 256, 10, 256, 256);
            graphics.drawString(font, name, -29 - nameWidth - totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, graphics, -11 - totalWidth, 6, time);
        } else
        {
            graphics.blit(DECORATIONS, -218 + nameWidth + totalWidth, 0, 0, 0, 256, 12, 256, 256);
            graphics.blit(DECORATIONS, -230 + totalWidth, 1, 0, 12, 256, 10, 256, 256);
            graphics.drawString(font, name, 30 + totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, graphics, 11 + totalWidth, 6, time);
        }
    }

    private static void renderFlat(IPickupEntry pickup, GuiGraphics graphics, Font font,
                                   MutableComponent name, int nameWidth,
                                   MutableComponent total, int totalWidth,
                                   long time)
    {
        if (isReversed())
        {
            graphics.fill(-3, 0, -25 - nameWidth - totalWidth, 12, 0x80000000);
            graphics.drawString(font, name, -23 - nameWidth - totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, graphics, -11 - totalWidth, 6, time);
        } else
        {
            graphics.fill(3, 0, 25 + nameWidth + totalWidth, 12, 0x80000000);
            graphics.drawString(font, name, 23 + totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, graphics, 11 + totalWidth, 6, time);
        }
    }

    private static void renderText(IPickupEntry pickup, GuiGraphics graphics, Font font,
                                   MutableComponent name, int nameWidth,
                                   MutableComponent total, int totalWidth,
                                   long time)
    {
        if (isReversed())
        {
            graphics.drawString(font, name, -23 - nameWidth - totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, graphics, -11 - totalWidth, 6, time);
        } else
        {
            graphics.drawString(font, name, 23 + totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, graphics, 11 + totalWidth, 6, time);
        }
    }

    @SuppressWarnings("all")
    private static void renderIcon(IPickupEntry pickup, GuiGraphics graphics, double x, double y, long time)
    {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        pickup.renderIcon(graphics, time);
        graphics.pose().popPose();
    }

    private static double getX(double progress)
    {
        final var offset = -220.0 + 220.0 * (1 - Math.pow(progress - 1, 2));
        return isReversed() ? -offset : offset;
    }

    private static boolean isReversed()
    {
        return LootJournal.CONFIG.anchor.isReversed();
    }

    @FunctionalInterface
    private interface Renderer
    {
        void render(IPickupEntry pickup, GuiGraphics graphics, Font font,
                    MutableComponent name, int nameWidth,
                    MutableComponent total, int totalWidth,
                    long time);
    }
}
