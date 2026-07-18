package com.leclowndu93150.thaumcraft.client.golem;

import com.leclowndu93150.thaumcraft.content.entity.EntityGolemDart;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

public final class GolemDartRenderer extends ArrowRenderer<EntityGolemDart> {
    public GolemDartRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGolemDart entity) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
