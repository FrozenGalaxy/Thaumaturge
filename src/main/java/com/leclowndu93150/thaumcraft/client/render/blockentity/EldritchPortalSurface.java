package com.leclowndu93150.thaumcraft.client.render.blockentity;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.render.TCShaders;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class EldritchPortalSurface {
    public static final ResourceLocation TUNNEL_TEXTURE = TCIds.rl("textures/misc/tunnel.png");
    public static final ResourceLocation PARTICLE_FIELD_TEXTURE = TCIds.rl("textures/misc/particlefield.png");

    public static final RenderType SURFACE = RenderType.create(
            "tc_eldritch_portal_surface",
            DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 1536, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(TCShaders::portal))
                    .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                            .add(TUNNEL_TEXTURE, false, false)
                            .add(PARTICLE_FIELD_TEXTURE, false, false)
                            .build())
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false));

    private EldritchPortalSurface() {}

    public static void quad(PoseStack.Pose pose, VertexConsumer buffer,
                            float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            float x3, float y3, float z3,
                            float x4, float y4, float z4) {
        Matrix4f mat = pose.pose();
        buffer.addVertex(mat, x1, y1, z1);
        buffer.addVertex(mat, x2, y2, z2);
        buffer.addVertex(mat, x3, y3, z3);
        buffer.addVertex(mat, x4, y4, z4);
    }
}
