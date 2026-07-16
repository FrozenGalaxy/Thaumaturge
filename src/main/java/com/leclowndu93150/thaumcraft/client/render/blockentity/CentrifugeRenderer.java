package com.leclowndu93150.thaumcraft.client.render.blockentity;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.entity.TCModelLayers;
import com.leclowndu93150.thaumcraft.client.model.entity.CentrifugeModel;
import com.leclowndu93150.thaumcraft.content.essentia.BlockEntityCentrifuge;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public final class CentrifugeRenderer implements BlockEntityRenderer<BlockEntityCentrifuge> {
    private static final ResourceLocation TEXTURE = TCIds.rl("textures/entity/centrifuge.png");

    private final CentrifugeModel model;

    public CentrifugeRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new CentrifugeModel(context.bakeLayer(TCModelLayers.CENTRIFUGE));
    }

    @Override
    public void render(BlockEntityCentrifuge centrifuge, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light, int overlay) {
        float rotation = centrifuge.rotation + centrifuge.rotationSpeed * partialTick;
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        model.boxes.render(poseStack, buffers.getBuffer(RenderType.entityCutout(TEXTURE)), light, overlay);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        model.spinner.render(poseStack, buffers.getBuffer(RenderType.entityCutout(TEXTURE)), light, overlay);
        poseStack.popPose();
    }
}
