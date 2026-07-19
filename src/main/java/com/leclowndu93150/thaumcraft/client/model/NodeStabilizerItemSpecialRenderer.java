package com.leclowndu93150.thaumcraft.client.model;

import com.leclowndu93150.thaumcraft.client.render.blockentity.NodeStabilizerRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class NodeStabilizerItemSpecialRenderer extends BlockEntityWithoutLevelRenderer {
    private final boolean advanced;

    public NodeStabilizerItemSpecialRenderer(boolean advanced) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.advanced = advanced;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffers, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
        NodeStabilizerRenderer.submitParts(0, advanced, 0.0F, poseStack, buffers, light);
        poseStack.popPose();
    }
}
