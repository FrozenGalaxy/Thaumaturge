package com.leclowndu93150.thaumcraft.client.model.obj;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class MeshQuadBaker {
    private static final float CONTRACT_EPS = 1.0F / 256.0F;
    private static final float CONTRACT_DIVISOR = 256.0F;
    private static final float NORMAL_EPS = 1.0E-6F;

    private MeshQuadBaker() {}

    public static void bakePart(MeshModel mesh, MeshPart part, TextureAtlasSprite sprite, int tintIndex,
                                Matrix4f transform, List<BakedQuad> output) {
        bakePart(mesh, part, sprite, tintIndex, transform, false, 0, 0, output);
    }

    public static void bakePart(MeshModel mesh, MeshPart part, TextureAtlasSprite sprite, int tintIndex,
                                Matrix4f transform, boolean flipV, List<BakedQuad> output) {
        bakePart(mesh, part, sprite, tintIndex, transform, flipV, 0, 0, output);
    }

    public static void bakePart(MeshModel mesh, MeshPart part, TextureAtlasSprite sprite, int tintIndex,
                                Matrix4f transform, boolean flipV, int blockLight, int skyLight, List<BakedQuad> output) {
        QuadBakingVertexConsumer consumer = new QuadBakingVertexConsumer();
        for (int i = 0; i + 3 < part.indices().size(); i += 4) {
            output.add(bakeQuad(mesh, part, i, sprite, transform, tintIndex, flipV, blockLight, skyLight, consumer));
        }
    }

    private static BakedQuad bakeQuad(MeshModel mesh, MeshPart part, int start, TextureAtlasSprite sprite,
                                      Matrix4f transform, int tintIndex, boolean flipV, int blockLight, int skyLight,
                                      QuadBakingVertexConsumer consumer) {
        Vector3f[] positions = new Vector3f[4];
        float[] us = new float[4];
        float[] vs = new float[4];
        for (int i = 0; i < 4; i++) {
            int[] vertex = part.indices().get(start + i);
            Vector3f pos = new Vector3f();
            Vector2f tex = new Vector2f();
            int slot = 0;
            if (mesh.positions() != null && vertex.length > slot && vertex[slot] >= 0) {
                pos.set(mesh.positions().get(vertex[slot]));
            }
            slot++;
            if (mesh.texCoords() != null && vertex.length > slot && vertex[slot] >= 0) {
                tex.set(mesh.texCoords().get(vertex[slot]));
            }
            pos.mulPosition(transform);
            positions[i] = pos;
            us[i] = sprite.getU(tex.x);
            vs[i] = sprite.getV(flipV ? 1.0F - tex.y : tex.y);
        }
        contractUvs(us, vs, sprite);

        Vector3f normal = faceNormal(positions);
        Direction facing;
        if (normal.lengthSquared() < NORMAL_EPS) {
            facing = Direction.UP;
            normal.set(0.0F, 1.0F, 0.0F);
        } else {
            normal.normalize();
            facing = Direction.getNearest(normal.x, normal.y, normal.z);
        }

        consumer.setSprite(sprite);
        consumer.setDirection(facing);
        consumer.setTintIndex(tintIndex);
        consumer.setShade(false);
        consumer.setHasAmbientOcclusion(false);
        for (int i = 0; i < 4; i++) {
            consumer.addVertex(positions[i].x, positions[i].y, positions[i].z)
                    .setColor(255, 255, 255, 255)
                    .setUv(us[i], vs[i])
                    .setUv2(blockLight, skyLight)
                    .setNormal(normal.x, normal.y, normal.z);
        }
        return consumer.bakeQuad();
    }

    private static void contractUvs(float[] us, float[] vs, TextureAtlasSprite sprite) {
        float texelsU = sprite.contents().width() / (sprite.getU1() - sprite.getU0());
        float texelsV = sprite.contents().height() / (sprite.getV1() - sprite.getV0());
        float texels = Math.max(texelsU, texelsV);
        float minShift = 1.0F / (texels * CONTRACT_DIVISOR);
        float centerU = (us[0] + us[1] + us[2] + us[3]) / 4.0F;
        float centerV = (vs[0] + vs[1] + vs[2] + vs[3]) / 4.0F;
        for (int i = 0; i < 4; i++) {
            us[i] = contract(us[i], centerU, minShift);
            vs[i] = contract(vs[i], centerV, minShift);
        }
    }

    private static float contract(float value, float center, float minShift) {
        float contracted = value * (1.0F - CONTRACT_EPS) + center * CONTRACT_EPS;
        if (Math.abs(value - contracted) >= minShift) {
            return contracted;
        }
        float toCenter = center - value;
        if (Math.abs(toCenter) < minShift) {
            return (value + center) / 2.0F;
        }
        return value + (toCenter < 0.0F ? -minShift : minShift);
    }

    public static Vector3f faceNormal(Vector3fc[] positions) {
        Vector3f edge1 = new Vector3f(positions[1]).sub(positions[0]);
        Vector3f edge2 = new Vector3f(positions[2]).sub(positions[0]);
        Vector3f normal = new Vector3f();
        edge1.cross(edge2, normal);
        return normal;
    }

    public static Direction computeFaceDirection(Vector3fc[] positions) {
        Vector3f normal = faceNormal(positions);
        if (normal.lengthSquared() < NORMAL_EPS) {
            return Direction.UP;
        }
        normal.normalize();
        return Direction.getNearest(normal.x, normal.y, normal.z);
    }
}
