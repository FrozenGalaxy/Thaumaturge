package com.leclowndu93150.thaumcraft.client.render;

import com.leclowndu93150.thaumcraft.client.fx.render.pipeline.TCRenderPipelines;
import java.util.function.Function;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;

public final class TCFlatRenderTypes {
    private static final Function<ResourceLocation, RenderType> CUTOUT_FLAT = Util.memoize(
            texture -> RenderType.create("tc_cutout_flat",
                    RenderSetup.builder(TCRenderPipelines.ENTITY_CUTOUT_FLAT)
                            .withTexture("Sampler0", texture)
                            .useLightmap()
                            .createRenderSetup()));

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_FLAT = Util.memoize(
            texture -> RenderType.create("tc_translucent_flat",
                    RenderSetup.builder(TCRenderPipelines.ENTITY_TRANSLUCENT_FLAT)
                            .withTexture("Sampler0", texture)
                            .useLightmap()
                            .sortOnUpload()
                            .createRenderSetup()));

    private static final Function<ResourceLocation, RenderType> ADDITIVE_FLAT = Util.memoize(
            texture -> RenderType.create("tc_additive_flat",
                    RenderSetup.builder(TCRenderPipelines.ENTITY_ADDITIVE_EMISSIVE)
                            .withTexture("Sampler0", texture)
                            .useLightmap()
                            .sortOnUpload()
                            .createRenderSetup()));

    private TCFlatRenderTypes() {}

    public static RenderType entityCutoutFlat(ResourceLocation texture) {
        return CUTOUT_FLAT.apply(texture);
    }

    public static RenderType entityTranslucentFlat(ResourceLocation texture) {
        return TRANSLUCENT_FLAT.apply(texture);
    }

    public static RenderType entityAdditiveFlat(ResourceLocation texture) {
        return ADDITIVE_FLAT.apply(texture);
    }
}
