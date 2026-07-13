package com.leclowndu93150.thaumcraft.client.fx.render.rendertype;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.fx.render.pipeline.TCFXPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public final class BoltRenderType {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/essentia.png");

    public static final RenderPipeline PIPELINE = TCFXPipelines.additiveTextured(
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "pipeline/bolt"));

    public static final RenderType RENDER_TYPE = RenderType.create(
            "thaumcraft_bolt",
            RenderSetup.builder(PIPELINE)
                    .withTexture("Sampler0", TEXTURE)
                    .createRenderSetup());

    @SubscribeEvent
    static void register(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(PIPELINE);
    }

    private BoltRenderType() {}
}
