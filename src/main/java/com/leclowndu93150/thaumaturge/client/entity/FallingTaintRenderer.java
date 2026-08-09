package com.leclowndu93150.thaumaturge.client.entity;

import com.leclowndu93150.thaumaturge.content.entity.EntityFallingTaint;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public final class FallingTaintRenderer extends EntityRenderer<EntityFallingTaint> {
    public FallingTaintRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFallingTaint entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
