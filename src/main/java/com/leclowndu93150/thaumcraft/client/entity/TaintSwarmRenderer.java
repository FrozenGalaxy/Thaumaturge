package com.leclowndu93150.thaumcraft.client.entity;

import com.leclowndu93150.thaumcraft.content.entity.EntityTaintSwarm;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public final class TaintSwarmRenderer extends EntityRenderer<EntityTaintSwarm> {

    public TaintSwarmRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTaintSwarm entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
