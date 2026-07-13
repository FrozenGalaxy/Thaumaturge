package com.leclowndu93150.thaumcraft;

import net.minecraft.resources.ResourceLocation;

public final class TCIds {
    public static final String MODID = "thaumcraft";
    public static final String CURIOS = "curios";

    private TCIds() {}

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
