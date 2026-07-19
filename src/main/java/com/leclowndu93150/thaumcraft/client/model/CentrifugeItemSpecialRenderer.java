package com.leclowndu93150.thaumcraft.client.model;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.entity.TCModelLayers;
import com.leclowndu93150.thaumcraft.client.model.entity.CentrifugeModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class CentrifugeItemSpecialRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE = TCIds.rl("textures/entity/centrifuge.png");

    private final CentrifugeModel model;

    public CentrifugeItemSpecialRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.model = new CentrifugeModel(Minecraft.getInstance().getEntityModels().bakeLayer(TCModelLayers.CENTRIFUGE));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffers, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        model.root.render(poseStack, buffers.getBuffer(RenderType.entityCutout(TEXTURE)), light, overlay);
        poseStack.popPose();
    }
}
