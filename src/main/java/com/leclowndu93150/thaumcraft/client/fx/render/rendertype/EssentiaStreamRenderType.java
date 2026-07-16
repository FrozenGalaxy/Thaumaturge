package com.leclowndu93150.thaumcraft.client.fx.render.rendertype;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.render.TCRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class EssentiaStreamRenderType {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/essentia.png");

    public static final RenderType RENDER_TYPE = TCRenderTypes.translucentTextured(TEXTURE);

    private EssentiaStreamRenderType() {}
}
