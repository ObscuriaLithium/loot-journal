package com.obscuria.lootjournal.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.obscuria.lootjournal.LootJournalConfig;
import com.obscuria.lootjournal.client.pickup.ItemStackPickup;
import com.obscuria.lootjournal.client.pickup.Pickup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber({Dist.CLIENT})
public final class Renderer {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderOverlay(RenderGuiOverlayEvent event) {
        if (!event.getOverlay().id().equals(new ResourceLocation("hotbar"))) return;
        final List<Pickup> LIST = PickupHandler.getList();
        if (!LIST.isEmpty()) {
            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            int i = 0;
            for (Pickup pickup : List.copyOf(LIST)) {
                if (pickup instanceof ItemStackPickup pickupItem) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.setShaderTexture(0, new ResourceLocation("loot_journal:textures/gui/pickup.png"));
                    //
                    final float posX = width - 16 - Minecraft.getInstance().font.width(" " + pickupItem.total + " ") +
                            Mth.lerp(Minecraft.getInstance().getFrameTime(), pickupItem.offsetLerp, pickupItem.offset);
                    final float posY = (height - LootJournalConfig.Client.offset.get() - 17 * (LIST.size() - i));
                    String name = (pickupItem.count > 1 ? pickupItem.count + "x " : "") + pickupItem.STACK.getHoverName().getString();
                    if (name.length() > 24)
                        name = name.substring(0, 23) + "...";
                    final int offset = Minecraft.getInstance().font.width(name);
                    if (LootJournalConfig.Client.style.get() == LootJournalConfig.PickupStyle.COMMON) {
                        GuiComponent.blit(event.getPoseStack(), (int) posX - 9 - offset, (int) posY + 2, 0, 0,
                                180, 12, 180, 26);
                        GuiComponent.blit(event.getPoseStack(), (int) posX - 2, (int) posY + 1, 0, 12,
                                180, 14, 180, 26);
                    }
                    final Style style = pickupItem.STACK.getRarity().getStyleModifier().apply(Style.EMPTY);
                    final ChatFormatting CUSTOM_COLOR = LootJournalConfig.Client.customColor.get();
                    final int color = LootJournalConfig.Client.useCustomColor.get() && CUSTOM_COLOR.getColor() != null
                            ? CUSTOM_COLOR.getColor() : style.getColor() != null ? style.getColor().getValue() : 0;

                    Minecraft.getInstance().font.drawShadow(event.getPoseStack(), name, posX - 3 - offset, posY + 4, color);
                    Minecraft.getInstance().font.drawShadow(event.getPoseStack(), "" + pickupItem.total, posX + 20, posY + 4,
                            ChatFormatting.GRAY.getColor() != null ? ChatFormatting.GRAY.getColor() : 0);
                    RenderSystem.depthMask(true);
                    Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(pickupItem.STACK, (int) posX, (int) posY);
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.enableDepthTest();
                    RenderSystem.disableBlend();
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    i++;
                }
            }
        }
    }
}
