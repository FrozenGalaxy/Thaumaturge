package com.leclowndu93150.thaumcraft.client.render.crystal;

import com.leclowndu93150.thaumcraft.client.model.obj.MeshModel;
import com.leclowndu93150.thaumcraft.client.model.obj.MeshPart;
import com.leclowndu93150.thaumcraft.client.model.obj.MeshQuadBaker;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;

public final class CrystalQuadBaker {
    private static final int FULLBRIGHT = 240;

    private CrystalQuadBaker() {}

    public static void bakePart(MeshModel mesh, MeshPart part, TextureAtlasSprite sprite, int tintIndex,
                                Matrix4f transform, List<BakedQuad> output) {
        MeshQuadBaker.bakePart(mesh, part, sprite, tintIndex, transform, false, FULLBRIGHT, FULLBRIGHT, output);
    }
}
