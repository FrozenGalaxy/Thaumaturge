package com.leclowndu93150.thaumaturge.client.effect.rendertype;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.client.render.TCRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class ArcRenderType {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beamh.png");

    public static final RenderType RENDER_TYPE = TCRenderTypes.additiveTextured(TEXTURE);

    private ArcRenderType() {}
}
