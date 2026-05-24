package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.v2.api.common.Color;
import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.Optional;

public record RayGlowEffect(
        Var<Color> primaryColor,
        Var<Color> secondaryColor,
        Var<Float> scale
) implements IconEffect {

    public static final Identifier TEXTURE;
    public static final MapCodec<RayGlowEffect> CODEC;

    private static final RenderPipeline RAY_GLOW_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(LootJournal.identifier("pipeline/ray_glow"))
                    .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.ADDITIVE), ColorTargetState.WRITE_ALL))
                    .withDepthStencilState(Optional.of(new DepthStencilState(CompareOp.ALWAYS_PASS, false, 0f, 0f)))
                    .withCull(false)
                    .build());

    @Override
    public MapCodec<RayGlowEffect> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphicsExtractor extractor, PickupRenderer pickup) {
        if (!Config.RAY_GLOW_ENABLED.get()) return;

        var primaryColor = this.primaryColor.get();
        var secondaryColor = this.secondaryColor.get();

        final var time = pickup.timeInSeconds();
        final var baseScale = Mth.clamp(Easing.EASE_OUT_CUBIC.compute(time / 0.5f), 0f, 1f);
        final var animatedScale = baseScale + Math.max(0f, Easing.EASE_IN_CUBIC.mergeOut(Easing.EASE_OUT_CUBIC, 0.35f).compute(time));

        extractor.pose().pushMatrix();
        extractor.pose().scale(scale.get(), scale.get());

        renderSegment(extractor, pickup, animatedScale, 0.5f, time, primaryColor);
        renderSegment(extractor, pickup, animatedScale * 0.75f, -0.33f, time, primaryColor.lerp(secondaryColor, 0.5f));
        renderSegment(extractor, pickup, animatedScale * 0.5f, 0.25f, time, secondaryColor);

        extractor.pose().popMatrix();
    }

    private void renderSegment(GuiGraphicsExtractor extractor, PickupRenderer pickup, float scale, float rotDelta, float timer, Color color) {
        final float angle = rotDelta * 3f + rotDelta * timer;

        extractor.pose().pushMatrix();
        extractor.pose().scale(scale, scale);
        extractor.pose().rotate(angle);

        extractor.blit(RAY_GLOW_PIPELINE, TEXTURE, -32, -32, 0f, 0f, 64, 64, 64, 64, color.argb());

        extractor.pose().popMatrix();
    }

    static {
        TEXTURE = LootJournal.identifier("textures/gui/effect_ray_glow.png");
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Var.COLOR.fieldOf("primary_color").forGetter(RayGlowEffect::primaryColor),
                Var.COLOR.fieldOf("secondary_color").forGetter(RayGlowEffect::secondaryColor),
                Var.FLOAT.fieldOf("scale").forGetter(RayGlowEffect::scale)
        ).apply(codec, RayGlowEffect::new));
    }
}