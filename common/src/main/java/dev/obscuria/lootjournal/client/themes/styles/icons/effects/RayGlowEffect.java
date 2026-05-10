package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public record RayGlowEffect(
        Var<ARGB> primaryColor,
        Var<ARGB> secondaryColor,
        Var<Float> scale
) implements IconEffect {

    public static final Identifier TEXTURE;
    public static final MapCodec<RayGlowEffect> CODEC;

    private static final RenderPipeline RAY_GLOW_PIPELINE = RenderPipeline
            .builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation(LootJournal.identifier("pipeline/ray_glow"))
            .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withCull(false)
            .build();

    @Override
    public MapCodec<RayGlowEffect> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
        if (!Config.RAY_GLOW_ENABLED.get()) return;

        var primaryColor = this.primaryColor.get();
        var secondaryColor = this.secondaryColor.get();

        final var time = pickup.timeInSeconds();
        final var baseScale = Mth.clamp(Easing.EASE_OUT_CUBIC.compute(time / 0.5f), 0f, 1f) * 2.0f;
        final var animatedScale = baseScale + Math.max(0f,
                Easing.EASE_IN_CUBIC.mergeOut(Easing.EASE_OUT_CUBIC, 0.35f).compute(time));

        graphics.pose().pushMatrix();
        graphics.pose().scale(scale.get(), scale.get());

        renderSegment(graphics, pickup, animatedScale, 0.5f, time, primaryColor);
        renderSegment(graphics, pickup, animatedScale * 0.75f, -0.33f, time, primaryColor.lerp(secondaryColor, 0.5f));
        renderSegment(graphics, pickup, animatedScale * 0.5f, 0.25f, time, secondaryColor);

        graphics.pose().popMatrix();
    }

    private void renderSegment(GuiGraphics graphics, PickupRenderer pickup, float scale, float rotDelta, float timer, ARGB color) {
        final float angle = rotDelta * 3f + rotDelta * timer;

        graphics.pose().pushMatrix();
        graphics.pose().scale(scale, scale);
        graphics.pose().rotate(angle);

        graphics.blit(RAY_GLOW_PIPELINE, TEXTURE, -32, -32, 0f, 0f, 64, 64, 64, 64, color.decimal());

        graphics.pose().popMatrix();
    }

    static {
        TEXTURE = LootJournal.identifier("textures/gui/effect_ray_glow.png");
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Var.ARGB.fieldOf("primary_color").forGetter(RayGlowEffect::primaryColor),
                Var.ARGB.fieldOf("secondary_color").forGetter(RayGlowEffect::secondaryColor),
                Var.FLOAT.fieldOf("scale").forGetter(RayGlowEffect::scale)
        ).apply(codec, RayGlowEffect::new));
    }
}