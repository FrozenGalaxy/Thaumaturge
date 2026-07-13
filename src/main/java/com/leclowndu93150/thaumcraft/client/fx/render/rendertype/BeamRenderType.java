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
public final class BeamRenderType {
    public static final ResourceLocation BEAM = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beam1.png");
    public static final ResourceLocation BEAML = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beaml.png");
    public static final ResourceLocation BEAMH = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beamh.png");
    public static final ResourceLocation NODE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/auranodes.png");

    public static final RenderPipeline PIPELINE = TCFXPipelines.additiveTextured(
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "pipeline/beam"));

    public static final RenderType TRUNK_BEAM = makeType("thaumcraft_beam_trunk_beam", BEAM);
    public static final RenderType TRUNK_BEAML = makeType("thaumcraft_beam_trunk_beaml", BEAML);
    public static final RenderType TRUNK_BEAMH = makeType("thaumcraft_beam_trunk_beamh", BEAMH);
    public static final RenderType NODE_TYPE = makeType("thaumcraft_beam_node", NODE);

    public static RenderType trunkForType(int type) {
        return switch (type) {
            case 1 -> TRUNK_BEAML;
            case 2 -> TRUNK_BEAMH;
            default -> TRUNK_BEAM;
        };
    }

    private static RenderType makeType(String name, ResourceLocation texture) {
        return RenderType.create(name,
                RenderSetup.builder(PIPELINE)
                        .withTexture("Sampler0", texture)
                        .createRenderSetup());
    }

    @SubscribeEvent
    static void register(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(PIPELINE);
    }

    private BeamRenderType() {}
}
