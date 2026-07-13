package com.leclowndu93150.thaumcraft.client.model.obj;

import java.io.IOException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public final class TCObjLoader {
    private TCObjLoader() {}

    public static MeshModel load(ResourceManager resources, ResourceLocation location) throws IOException {
        return WavefrontObject.load(resources, location).model();
    }
}
