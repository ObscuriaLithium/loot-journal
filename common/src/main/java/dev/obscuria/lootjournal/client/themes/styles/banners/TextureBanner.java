package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public record TextureBanner(
        Identifier texture,
        int textureWidth,
        int textureHeight,
        int uOffset,
        int vOffset,
        int uWidth,
        int vHeight,
        int pivotX,
        int pivotY
) implements PickupBanner {

    public static final MapCodec<TextureBanner> CODEC;

    @Override
    public MapCodec<TextureBanner> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer pickup) {
        int x = pickup.isMirrored() ? -(uWidth - pivotX) : -pivotX;
        int y = -pivotY;

        float u0 = (float) uOffset / textureWidth;
        float u1 = (float)(uOffset + uWidth) / textureWidth;
        float v0 = (float) vOffset / textureHeight;
        float v1 = (float)(vOffset + vHeight) / textureHeight;

        if (pickup.isMirrored()) {
            float tmp = u0;
            u0 = u1;
            u1 = tmp;
        }

        extractor.blit(texture, x, y, x + uWidth, y + vHeight, u0, u1, v0, v1);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Identifier.CODEC.fieldOf("texture").forGetter(TextureBanner::texture),
                Codec.INT.fieldOf("texture_width").forGetter(TextureBanner::textureWidth),
                Codec.INT.fieldOf("texture_height").forGetter(TextureBanner::textureHeight),
                Codec.INT.fieldOf("u_offset").forGetter(TextureBanner::uOffset),
                Codec.INT.fieldOf("v_offset").forGetter(TextureBanner::vOffset),
                Codec.INT.fieldOf("u_width").forGetter(TextureBanner::uWidth),
                Codec.INT.fieldOf("v_height").forGetter(TextureBanner::vHeight),
                Codec.INT.fieldOf("pivot_x").forGetter(TextureBanner::pivotX),
                Codec.INT.fieldOf("pivot_y").forGetter(TextureBanner::pivotY)
        ).apply(codec, TextureBanner::new));
    }
}