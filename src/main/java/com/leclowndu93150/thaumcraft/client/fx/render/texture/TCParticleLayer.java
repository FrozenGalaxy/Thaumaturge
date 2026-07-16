package com.leclowndu93150.thaumcraft.client.fx.render.texture;

import com.leclowndu93150.thaumcraft.TCIds;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public final class TCParticleLayer {
    public static final ResourceLocation SHEET = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/particle/fx_generic.png");
    public static final ResourceLocation NITOR_CORE_SHEET = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/particle/nitor_core.png");

    public static final ParticleRenderType ADDITIVE = additive(SHEET, true);
    public static final ParticleRenderType TRANSLUCENT = translucent(SHEET, true);
    public static final ParticleRenderType ADDITIVE_NO_DEPTH = additive(SHEET, false);
    public static final ParticleRenderType TRANSLUCENT_NO_DEPTH = translucent(SHEET, false);
    public static final ParticleRenderType NITOR_CORE = translucent(NITOR_CORE_SHEET, true);

    private TCParticleLayer() {}

    public static ParticleRenderType byLayer(int layer) {
        return switch (layer) {
            case 1 -> TRANSLUCENT;
            case 2 -> ADDITIVE_NO_DEPTH;
            case 3 -> TRANSLUCENT_NO_DEPTH;
            default -> ADDITIVE;
        };
    }

    private static ParticleRenderType additive(ResourceLocation sheet, boolean depthMask) {
        return (tesselator, textureManager) -> {
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderTexture(0, sheet);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.depthMask(depthMask);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        };
    }

    private static ParticleRenderType translucent(ResourceLocation sheet, boolean depthMask) {
        return (tesselator, textureManager) -> {
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderTexture(0, sheet);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(depthMask);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        };
    }
}
