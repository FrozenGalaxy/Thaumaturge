package com.leclowndu93150.thaumaturge.client.entity;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.client.model.entity.TaintacleModel;
import com.leclowndu93150.thaumaturge.content.entity.AbstractTaintacle;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class TaintacleRenderer extends MobRenderer<AbstractTaintacle, TaintacleModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/entity/taintacle.png");
    private static final float MODEL_Y_OFFSET = 1.501F;
    private static final float TALL_HEIGHT = 3.0F;
    private static final float TALL_LIFT = 0.6F;
    private static final float SHORT_LIFT = 1.2F;
    private static final float HEIGHT_SCALE_DIVISOR = 3.0F;
    private static final float EMERGE_TICKS_PER_HEIGHT = 10.0F;

    public TaintacleRenderer(
            EntityRendererProvider.Context context, ModelLayerLocation layer, int length, float shadow) {
        super(context, new TaintacleModel(context.bakeLayer(layer), length), shadow);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractTaintacle entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(AbstractTaintacle entity, PoseStack poseStack, float partialTick) {
        float height = entity.getBbHeight();
        float ageInTicks = entity.tickCount + partialTick;
        float emerge = 0.0F;
        float emergeTicks = height * EMERGE_TICKS_PER_HEIGHT;
        if (ageInTicks < emergeTicks) {
            emerge = (emergeTicks - ageInTicks) / emergeTicks * height;
        }
        float lift = (height == TALL_HEIGHT ? TALL_LIFT : SHORT_LIFT) + emerge;
        float s = height / HEIGHT_SCALE_DIVISOR;
        poseStack.translate(0.0F, lift - MODEL_Y_OFFSET * (1.0F - s), 0.0F);
        poseStack.scale(s, s, s);
    }
}
