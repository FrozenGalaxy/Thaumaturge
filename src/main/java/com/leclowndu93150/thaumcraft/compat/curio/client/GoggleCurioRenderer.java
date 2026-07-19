package com.leclowndu93150.thaumcraft.compat.curio.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class GoggleCurioRenderer implements ICurioRenderer {

    private HumanoidModel<LivingEntity> armorModel;

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers, int light,
            float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(stack.getItem() instanceof ArmorItem armorItem)) {
            return;
        }
        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?>)) {
            return;
        }
        LivingEntity wearer = slotContext.entity();
        if (wearer == null) {
            return;
        }
        HumanoidModel<LivingEntity> model = armorModel();
        ICurioRenderer.followBodyRotations(wearer, model);
        model.setAllVisible(false);
        model.head.visible = true;
        model.hat.visible = true;
        ArmorMaterial material = armorItem.getMaterial().value();
        for (ArmorMaterial.Layer layer : material.layers()) {
            VertexConsumer buffer = buffers.getBuffer(RenderType.armorCutoutNoCull(layer.texture(false)));
            model.renderToBuffer(poseStack, buffer, light, OverlayTexture.NO_OVERLAY, -1);
        }
    }

    private HumanoidModel<LivingEntity> armorModel() {
        if (armorModel == null) {
            ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR);
            armorModel = new HumanoidModel<>(part);
        }
        return armorModel;
    }
}
