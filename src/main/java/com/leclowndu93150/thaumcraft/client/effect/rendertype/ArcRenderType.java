package com.leclowndu93150.thaumcraft.client.effect.rendertype;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.render.TCRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class ArcRenderType {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "textures/effect/beamh.png");

    public static final RenderType RENDER_TYPE = TCRenderTypes.additiveTextured(TEXTURE);

    private ArcRenderType() {}
}
