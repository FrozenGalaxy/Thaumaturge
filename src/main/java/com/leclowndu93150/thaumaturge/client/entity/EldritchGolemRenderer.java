package com.leclowndu93150.thaumaturge.client.entity;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.client.model.entity.EldritchGolemModel;
import com.leclowndu93150.thaumaturge.content.entity.boss.EntityEldritchGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class EldritchGolemRenderer extends MobRenderer<EntityEldritchGolem, EldritchGolemModel> {
    private static final ResourceLocation TEXTURE = TCIds.rl("textures/entity/eldritch_golem.png");
    private static final float SHADOW = 0.7F;
    private static final float SCALE = 1.8F;

    public EldritchGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchGolemModel(context.bakeLayer(TCModelLayers.ELDRITCH_GOLEM)), SHADOW);
    }

    @Override
    protected void scale(EntityEldritchGolem entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEldritchGolem entity) {
        return TEXTURE;
    }
}
