package com.leclowndu93150.thaumcraft.client.particle;

import com.leclowndu93150.thaumcraft.Thaumcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public final class ParticleSheet {
    private static final int PNG_DIMENSION_OFFSET = 16;

    public enum Mode {
        ADDITIVE(true, true),
        TRANSLUCENT(false, true),
        ADDITIVE_NO_DEPTH(true, false),
        TRANSLUCENT_NO_DEPTH(false, false);

        final boolean additive;
        final boolean depthMask;

        Mode(boolean additive, boolean depthMask) {
            this.additive = additive;
            this.depthMask = depthMask;
        }
    }

    private final String name;
    private final ResourceLocation texture;
    private final Map<Mode, ParticleRenderType> layers = new ConcurrentHashMap<>();
    private volatile int frames;

    ParticleSheet(String name, ResourceLocation texture) {
        this.name = name;
        this.texture = texture;
    }

    public ResourceLocation texture() {
        return texture;
    }

    public int frames() {
        int resolved = frames;
        if (resolved == 0) {
            resolved = readFrameCount();
            frames = resolved;
        }
        return resolved;
    }

    public float u0(int frame) {
        return (float) frame / frames();
    }

    public float u1(int frame) {
        return (float) (frame + 1) / frames();
    }

    ParticleRenderType layer(Mode mode) {
        return layers.computeIfAbsent(mode, this::build);
    }

    private ParticleRenderType build(Mode mode) {
        return (tesselator, textureManager) -> {
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.enableBlend();
            if (mode.additive) {
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            } else {
                RenderSystem.defaultBlendFunc();
            }
            RenderSystem.depthMask(mode.depthMask);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        };
    }

    void invalidate() {
        frames = 0;
        layers.clear();
    }

    private int readFrameCount() {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().getResourceOrThrow(texture).open();
             DataInputStream data = new DataInputStream(stream)) {
            data.skipBytes(PNG_DIMENSION_OFFSET);
            int width = data.readInt();
            int height = data.readInt();
            return Math.max(1, width / Math.max(1, height));
        } catch (IOException e) {
            Thaumcraft.LOGGER.error("Cannot read particle sheet {}", texture, e);
            return 1;
        }
    }
}
