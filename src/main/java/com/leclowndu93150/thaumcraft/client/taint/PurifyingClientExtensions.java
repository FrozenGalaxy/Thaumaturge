package com.leclowndu93150.thaumcraft.client.taint;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public final class PurifyingClientExtensions implements IClientFluidTypeExtensions {
    private static final ResourceLocation STILL = ResourceLocation.withDefaultNamespace("block/water_still");
    private static final ResourceLocation FLOWING = ResourceLocation.withDefaultNamespace("block/water_flow");
    private static final ResourceLocation OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");
    private static final int TINT = 0x77FFEEAA;

    @Override
    public ResourceLocation getStillTexture() {
        return STILL;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return FLOWING;
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return OVERLAY;
    }

    @Override
    public int getTintColor() {
        return TINT;
    }
}
