package com.leclowndu93150.thaumcraft.client.effect.rendertype;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.render.TCRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class BeamRenderType {
    public static final ResourceLocation BEAM = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beam1.png");
    public static final ResourceLocation BEAML = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beaml.png");
    public static final ResourceLocation BEAMH = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beamh.png");
    public static final ResourceLocation NODE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/auranodes.png");

    public static final RenderType TRUNK_BEAM = TCRenderTypes.additiveTextured(BEAM);
    public static final RenderType TRUNK_BEAML = TCRenderTypes.additiveTextured(BEAML);
    public static final RenderType TRUNK_BEAMH = TCRenderTypes.additiveTextured(BEAMH);
    public static final RenderType NODE_TYPE = TCRenderTypes.additiveTextured(NODE);

    public static RenderType trunkForType(int type) {
        return switch (type) {
            case 1 -> TRUNK_BEAML;
            case 2 -> TRUNK_BEAMH;
            default -> TRUNK_BEAM;
        };
    }

    private BeamRenderType() {}
}
