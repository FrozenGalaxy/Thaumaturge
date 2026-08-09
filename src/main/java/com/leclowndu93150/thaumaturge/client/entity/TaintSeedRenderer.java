package com.leclowndu93150.thaumaturge.client.entity;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.client.model.entity.TaintSeedModel;
import com.leclowndu93150.thaumaturge.content.entity.AbstractTaintSeed;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class TaintSeedRenderer extends MobRenderer<AbstractTaintSeed, TaintSeedModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/entity/taint_seed.png");
    private static final float MODEL_Y_OFFSET = 1.501F;
    private static final float LIFT = 1.2F;
    private static final float HEIGHT_SCALE_DIVISOR = 2.0F;
    private static final float EMERGE_TICKS_PER_HEIGHT = 10.0F;

    public TaintSeedRenderer(EntityRendererProvider.Context context, float shadow) {
        super(context, new TaintSeedModel(context.bakeLayer(TCModelLayers.TAINT_SEED)), shadow);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractTaintSeed entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(AbstractTaintSeed entity, PoseStack poseStack, float partialTick) {
        float height = entity.getBbHeight();
        float ageInTicks = entity.tickCount + partialTick;
        float emerge = 0.0F;
        float emergeTicks = height * EMERGE_TICKS_PER_HEIGHT;
        if (ageInTicks < emergeTicks) {
            emerge = (emergeTicks - ageInTicks) / emergeTicks * height;
        }
        float s = height / HEIGHT_SCALE_DIVISOR;
        poseStack.translate(0.0F, LIFT + emerge - MODEL_Y_OFFSET * (1.0F - s), 0.0F);
        poseStack.scale(s, s, s);
    }
}
