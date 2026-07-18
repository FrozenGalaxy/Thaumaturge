package com.leclowndu93150.thaumcraft.client.entity;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.model.entity.ArcaneBoreModel;
import com.leclowndu93150.thaumcraft.client.render.TCRenderTypes;
import com.leclowndu93150.thaumcraft.content.entity.construct.EntityArcaneBore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public final class ArcaneBoreRenderer extends MobRenderer<EntityArcaneBore, ArcaneBoreModel> {
    private static final ResourceLocation TEXTURE = TCIds.rl("textures/entity/arcanebore.png");
    private static final ResourceLocation BEAM_TEXTURE = TCIds.rl("textures/misc/beam1.png");
    private static final RenderType BEAM_TYPE = TCRenderTypes.fxAdditive(BEAM_TEXTURE);

    private static final float SHADOW = 0.5F;
    private static final double BEAM_LENGTH = 5.0;
    private static final float BEAM_RADIUS = 0.15F;
    private static final float BEAM_ALPHA = 0.4F;
    private static final int BEAM_TINT = ARGB32.colorFromFloat(BEAM_ALPHA, 0.0F, 1.0F, 0.4F);
    private static final int BEAM_LIGHT = 0x000000C8;
    private static final int BEAM_STRIPS = 3;

    public ArcaneBoreRenderer(EntityRendererProvider.Context context) {
        super(context, new ArcaneBoreModel(context.bakeLayer(TCModelLayers.ARCANE_BORE)), SHADOW);
    }

    @Override
    public void render(EntityArcaneBore entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
        if (!(entity.clientDigging && entity.isActive() && entity.validInventory())) {
            return;
        }
        float headYaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float headPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float eyeHeight = entity.getEyeHeight();
        float animTicks = entity.tickCount + partialTicks;
        float beamScroll = (float) (entity.level().getGameTime() % 72L * 5L) + 5.0F * partialTicks;
        poseStack.pushPose();
        poseStack.translate(0.0F, eyeHeight, 0.0F);
        float scroll = -animTicks;
        float uvOffset = -scroll * 0.2F - Mth.floor(-scroll * 0.1F);
        poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F + headYaw));
        poseStack.mulPose(Axis.XN.rotationDegrees(headPitch));
        poseStack.mulPose(Axis.YP.rotationDegrees(beamScroll));
        VertexConsumer buffer = buffers.getBuffer(BEAM_TYPE);
        for (int strip = 0; strip < BEAM_STRIPS; strip++) {
            poseStack.mulPose(Axis.YP.rotationDegrees(60.0F));
            float v0 = -1.0F + uvOffset + strip / 3.0F;
            float v1 = (float) BEAM_LENGTH + v0;
            Matrix4f mat = poseStack.last().pose();
            buffer.addVertex(mat, 0.0F, (float) BEAM_LENGTH, 0.0F).setUv(1.0F, v1)
                    .setColor(BEAM_TINT).setLight(BEAM_LIGHT);
            buffer.addVertex(mat, -BEAM_RADIUS, 0.0F, 0.0F).setUv(1.0F, v0)
                    .setColor(BEAM_TINT).setLight(BEAM_LIGHT);
            buffer.addVertex(mat, BEAM_RADIUS, 0.0F, 0.0F).setUv(0.0F, v0)
                    .setColor(BEAM_TINT).setLight(BEAM_LIGHT);
            buffer.addVertex(mat, 0.0F, (float) BEAM_LENGTH, 0.0F).setUv(0.0F, v1)
                    .setColor(BEAM_TINT).setLight(BEAM_LIGHT);
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityArcaneBore entity) {
        return TEXTURE;
    }
}
