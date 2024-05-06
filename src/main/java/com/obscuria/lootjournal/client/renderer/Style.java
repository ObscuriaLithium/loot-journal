package com.obscuria.lootjournal.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.obscuria.lootjournal.client.pickup.Pickup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public enum Style {
    DEFAULT(Style::renderStyleDefault),
    FLAT(Style::renderStyleFlat),
    NO_BACKGROUND(Style::renderStyleNoBackground);

    private static final ResourceLocation DECORATIONS = new ResourceLocation("loot_journal:textures/gui/decorations.png");
    private final RenderFunc renderFunc;

    Style(RenderFunc renderFunc) {
        this.renderFunc = renderFunc;
    }

    public void render(Pickup pickup, PoseStack pose, Anchor anchor, int x, int y, double factor, long time) {
        final var font = Minecraft.getInstance().font;
        final var actualX = x + getOffset(factor, anchor.isInverted());
        final var name = pickup.getName();
        final var nameWidth = font.width(name);
        final var total = pickup.shouldRenderTotal()
                ? Component.literal(String.valueOf(pickup.getTotal()))
                : Component.empty();
        final var totalWidth = pickup.shouldRenderTotal() ? font.width(total) + 5 : 0;
        pose.pushPose();
        pose.translate(actualX, y, 400);
        RenderSystem.enableBlend();
        renderFunc.render(pickup, pose, anchor, font, name, nameWidth, total, totalWidth, actualX, y, time);
        RenderSystem.disableBlend();
        pose.popPose();
    }

    private static void renderStyleDefault(Pickup pickup, PoseStack pose, Anchor anchor, Font font,
                                           MutableComponent name, int nameWidth,
                                           MutableComponent total, int totalWidth,
                                           double x, double y, long time) {
        RenderSystem.setShaderTexture(0, DECORATIONS);
        if (anchor.isInverted()) {
            GuiComponent.blit(pose, -36 - nameWidth - totalWidth, 0, 0, 0, 256, 12, 256, 256);
            GuiComponent.blit(pose, -26 - totalWidth, 1, 0, 12, 256, 10, 256, 256);
            GuiComponent.drawString(pose, font, name, -29 - nameWidth - totalWidth, 2, 0xffffff);
            GuiComponent.drawString(pose, font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, pose, -11 - totalWidth, 6, time);
        } else {
            GuiComponent.blit(pose, -218 + nameWidth + totalWidth, 0, 0, 0, 256, 12, 256, 256);
            GuiComponent.blit(pose, -230 + totalWidth, 1, 0, 12, 256, 10, 256, 256);
            GuiComponent.drawString(pose, font, name, 30 + totalWidth, 2, 0xffffff);
            GuiComponent.drawString(pose, font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, pose, 11 + totalWidth, 6, time);
        }
    }

    private static void renderStyleFlat(Pickup pickup, PoseStack pose, Anchor anchor, Font font,
                                        MutableComponent name, int nameWidth,
                                        MutableComponent total, int totalWidth,
                                        double x, double y, long time) {
        if (anchor.isInverted()) {
            GuiComponent.fill(pose, -3, 0, -25 - nameWidth - totalWidth, 12, 0x80000000);
            GuiComponent.drawString(pose, font, name, -23 - nameWidth - totalWidth, 2, 0xffffff);
            GuiComponent.drawString(pose, font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, pose, -11 - totalWidth, 6, time);
        } else {
            GuiComponent.fill(pose, 3, 0, 25 + nameWidth + totalWidth, 12, 0x80000000);
            GuiComponent.drawString(pose, font, name, 23 + totalWidth, 2, 0xffffff);
            GuiComponent.drawString(pose, font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, pose, 11 + totalWidth, 6, time);
        }
    }

    private static void renderStyleNoBackground(Pickup pickup, PoseStack pose, Anchor anchor, Font font,
                                                MutableComponent name, int nameWidth,
                                                MutableComponent total, int totalWidth,
                                                double x, double y, long time) {
        if (anchor.isInverted()) {
            GuiComponent.drawString(pose, font, name, -23 - nameWidth - totalWidth, 2, 0xffffff);
            GuiComponent.drawString(pose, font, total.withStyle(ChatFormatting.GRAY), -totalWidth, 2, 0xffffff);
            renderIcon(pickup, pose, -11 - totalWidth, 6, time);
        } else {
            GuiComponent.drawString(pose, font, name, 23 + totalWidth, 2, 0xffffff);
            GuiComponent.drawString(pose, font, total.withStyle(ChatFormatting.GRAY), 5, 2, 0xffffff);
            renderIcon(pickup, pose, 11 + totalWidth, 6, time);
        }
    }

    @SuppressWarnings("all")
    private static void renderIcon(Pickup pickup, PoseStack pose, double x, double y, long time) {
        pose.pushPose();
        pose.translate(x, y, 0);
        pickup.renderIcon(pose, time);
        pose.popPose();
    }

    private static Component getName(String raw, int count) {
        if (raw.length() > 24) raw = raw.substring(0, 23) + "...";
        return Component.literal(raw + " x" + count);
    }

    private static double getOffset(double factor, boolean inverted) {
        final var offset = -220.0 + 220.0 * (1 - Math.pow(factor - 1, 2));
        return inverted ? -offset : offset;
    }

    @FunctionalInterface
    private interface RenderFunc {
        void render(Pickup pickup, PoseStack pose, Anchor anchor, Font font,
                    MutableComponent name, int nameWidth,
                    MutableComponent total, int totalWidth,
                    double x, double y, long time);
    }
}
