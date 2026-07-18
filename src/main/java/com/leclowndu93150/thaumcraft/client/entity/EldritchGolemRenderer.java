package com.leclowndu93150.thaumcraft.client.entity;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.model.entity.EldritchGolemModel;
import com.leclowndu93150.thaumcraft.content.entity.boss.EntityEldritchGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class EldritchGolemRenderer extends MobRenderer<EntityEldritchGolem, EldritchGolemModel> {
    private static final ResourceLocation TEXTURE = TCIds.rl("textures/entity/eldritch_golem.png");
    private static final float SHADOW = 0.7F;

    public EldritchGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new EldritchGolemModel(context.bakeLayer(TCModelLayers.ELDRITCH_GOLEM)), SHADOW);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEldritchGolem entity) {
        return TEXTURE;
    }
}
