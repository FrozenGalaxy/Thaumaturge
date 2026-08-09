package com.leclowndu93150.thaumaturge.client.model;

import com.leclowndu93150.thaumaturge.client.render.blockentity.GolemBuilderRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class GolemBuilderItemSpecialRenderer extends BlockEntityWithoutLevelRenderer {
    private static final float SCALE = 0.5F;
    private static final float MESH_CENTER_X = 0.549F;
    private static final float MESH_CENTER_Z = 0.5439F;

    public GolemBuilderItemSpecialRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffers, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(-MESH_CENTER_X, 0.0F, -MESH_CENTER_Z);
        GolemBuilderRenderer.submitParts(0, poseStack, buffers, light);
        poseStack.popPose();
    }
}
