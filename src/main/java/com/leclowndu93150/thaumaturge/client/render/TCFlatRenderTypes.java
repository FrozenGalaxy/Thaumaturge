package com.leclowndu93150.thaumaturge.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class TCFlatRenderTypes {
    private TCFlatRenderTypes() {}

    public static RenderType entityCutoutFlat(ResourceLocation texture) {
        return TCRenderTypes.entityCutoutFlat(texture);
    }

    public static RenderType entityTranslucentFlat(ResourceLocation texture) {
        return TCRenderTypes.entityTranslucentFlat(texture);
    }

    public static RenderType entityAdditiveFlat(ResourceLocation texture) {
        return TCRenderTypes.entityAdditiveEmissive(texture);
    }
}
