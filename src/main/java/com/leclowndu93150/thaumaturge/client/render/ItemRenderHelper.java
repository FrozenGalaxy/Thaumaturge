package com.leclowndu93150.thaumaturge.client.render;

import com.leclowndu93150.thaumaturge.client.render.blockentity.LegacyItemLift;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class ItemRenderHelper {
    private ItemRenderHelper() {}

    public static void render(
            ItemStack stack,
            ItemDisplayContext context,
            PoseStack pose,
            MultiBufferSource buffers,
            int light,
            int overlay,
            int seed) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getItemRenderer().renderStatic(stack, context, light, overlay, pose, buffers, minecraft.level, seed);
    }

    public static void renderGroundCentered(
            ItemStack stack, PoseStack pose, MultiBufferSource buffers, int light, int overlay, int seed) {
        pose.pushPose();
        pose.translate(0.0F, LegacyItemLift.centerLift(stack, ItemDisplayContext.GROUND), 0.0F);
        render(stack, ItemDisplayContext.GROUND, pose, buffers, light, overlay, seed);
        pose.popPose();
    }
}
