package com.leclowndu93150.thaumaturge.client.render.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor.ARGB32;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;
import org.jetbrains.annotations.NotNull;

public final class TintBufferSource implements MultiBufferSource {
    private final MultiBufferSource parent;
    private final int tint;

    public TintBufferSource(MultiBufferSource parent, int tint) {
        this.parent = parent;
        this.tint = tint;
    }

    @Override
    public @NotNull VertexConsumer getBuffer(RenderType renderType) {
        return new TintWrapper(parent.getBuffer(renderType), tint);
    }

    private static final class TintWrapper extends VertexConsumerWrapper {
        private final int tintRed;
        private final int tintGreen;
        private final int tintBlue;
        private final int tintAlpha;

        TintWrapper(VertexConsumer parent, int tint) {
            super(parent);
            this.tintRed = ARGB32.red(tint);
            this.tintGreen = ARGB32.green(tint);
            this.tintBlue = ARGB32.blue(tint);
            this.tintAlpha = ARGB32.alpha(tint);
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            return super.setColor(
                    red * tintRed / 255, green * tintGreen / 255, blue * tintBlue / 255, alpha * tintAlpha / 255);
        }
    }
}
