package dev.obscuria.lootjournal.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.pickup.IPickup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public enum Style
{
    CLASSIC(Style::renderClassic),
    FLAT(Style::renderFlat),
    TEXT(Style::renderText);

    private static final ResourceLocation DECORATIONS = LootJournal.key("textures/gui/decorations.png");
    private final Renderer renderer;

    Style(Renderer renderer)
    {
        this.renderer = renderer;
    }

    public void render(IPickup pickup, GuiGraphics graphics, Anchor anchor, int x, int y, double ratio, long time)
    {
        final var font = Minecraft.getInstance().font;
        final var actualX = x + getOffset(ratio, anchor.isReversed());
        final var name = pickup.getDisplayName();
        final var nameWidth = font.width(name);
        final var total = pickup.shouldDisplayTotal()
                ? Component.literal(String.valueOf(pickup.getTotal()))
                : Component.empty();
        final var totalWidth = pickup.shouldDisplayTotal() ? font.width(total) + 5 : 0;
        graphics.pose().pushPose();
        graphics.pose().translate(actualX, y, 400);
        RenderSystem.enableBlend();
        renderer.render(pickup, graphics, anchor, font, name, nameWidth, total, totalWidth, actualX, y, time);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }

    private static void renderClassic(IPickup pickup, GuiGraphics graphics, Anchor anchor, Font font,
                                      MutableComponent name, int nameWidth,
                                      MutableComponent total, int totalWidth,
                                      double x, double y, long time)
    {
        if (anchor.isReversed())
        {
            graphics.blit(DECORATIONS, -36 - nameWidth - totalWidth, 0, 0, 0, 256, 12, 256, 256);
            graphics.blit(DECORATIONS, -26 - totalWidth, 1, 0, 12, 256, 10, 256, 256);
            graphics.drawString(font, name, -29 - nameWidth - totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, graphics, -11 - totalWidth, 6, time);
        }
        else
        {
            graphics.blit(DECORATIONS, -218 + nameWidth + totalWidth, 0, 0, 0, 256, 12, 256, 256);
            graphics.blit(DECORATIONS, -230 + totalWidth, 1, 0, 12, 256, 10, 256, 256);
            graphics.drawString(font, name, 30 + totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, graphics, 11 + totalWidth, 6, time);
        }
    }

    private static void renderFlat(IPickup pickup, GuiGraphics graphics, Anchor anchor, Font font,
                                   MutableComponent name, int nameWidth,
                                   MutableComponent total, int totalWidth,
                                   double x, double y, long time)
    {
        if (anchor.isReversed())
        {
            graphics.fill(-3, 0, -25 - nameWidth - totalWidth, 12, 0x80000000);
            graphics.drawString(font, name, -23 - nameWidth - totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, graphics, -11 - totalWidth, 6, time);
        }
        else
        {
            graphics.fill(3, 0, 25 + nameWidth + totalWidth, 12, 0x80000000);
            graphics.drawString(font, name, 23 + totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, graphics, 11 + totalWidth, 6, time);
        }
    }

    private static void renderText(IPickup pickup, GuiGraphics graphics, Anchor anchor, Font font,
                                   MutableComponent name, int nameWidth,
                                   MutableComponent total, int totalWidth,
                                   double x, double y, long time)
    {
        if (anchor.isReversed())
        {
            graphics.drawString(font, name, -23 - nameWidth - totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, graphics, -11 - totalWidth, 6, time);
        }
        else
        {
            graphics.drawString(font, name, 23 + totalWidth, 2, 0xffffff);
            graphics.drawString(font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, graphics, 11 + totalWidth, 6, time);
        }
    }

    @SuppressWarnings("all")
    private static void renderIcon(IPickup pickup, GuiGraphics graphics, double x, double y, long time)
    {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        pickup.renderIcon(graphics, time);
        graphics.pose().popPose();
    }

    private static Component getName(String raw, int count)
    {
        if (raw.length() > 24) raw = raw.substring(0, 23) + "...";
        return Component.literal(raw + " x" + count);
    }

    private static double getOffset(double factor, boolean inverted)
    {
        final var offset = -220.0 + 220.0 * (1 - Math.pow(factor - 1, 2));
        return inverted ? -offset : offset;
    }

    @FunctionalInterface
    private interface Renderer
    {
        void render(IPickup pickup, GuiGraphics graphics,
                    Anchor anchor, Font font,
                    MutableComponent name, int nameWidth,
                    MutableComponent total, int totalWidth,
                    double x, double y, long time);
    }
}
