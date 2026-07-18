package com.leclowndu93150.thaumcraft.client.render.blockentity;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.entity.TCModelLayers;
import com.leclowndu93150.thaumcraft.client.model.entity.MatrixCubeModel;
import com.leclowndu93150.thaumcraft.client.render.TCRenderTypes;
import com.leclowndu93150.thaumcraft.content.infusion.BlockEntityInfusionMatrix;
import com.leclowndu93150.thaumcraft.registry.TCBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.joml.Matrix4f;

public final class InfusionMatrixRenderer implements BlockEntityRenderer<BlockEntityInfusionMatrix> {
    private static final ResourceLocation TEX_NORMAL = TCIds.rl("textures/block/infuser_normal.png");
    private static final ResourceLocation TEX_ANCIENT = TCIds.rl("textures/block/infuser_ancient.png");
    private static final ResourceLocation TEX_ELDRITCH = TCIds.rl("textures/block/infuser_eldritch.png");

    private static final float SUB_CUBE_OFFSET = 0.25F;
    private static final float SUB_CUBE_SCALE = 0.45F;
    private static final float TILT_X = 35.0F;
    private static final float TILT_Z = 45.0F;
    private static final float JITTER_SCALE = 0.01F;
    private static final float GLOW_RED = 0.8F;
    private static final float GLOW_GREEN = 0.1F;
    private static final float GLOW_BLUE = 1.0F;
    private static final long HALO_SEED = 245L;
    private static final int HALO_FANS_FANCY = 20;
    private static final int HALO_FANS_FAST = 10;
    private static final float HALO_FADE_TICKS = 500.0F;
    private static final float HALO_RAMP_TICKS = 50.0F;

    private static final RenderType GLOW_NORMAL = TCRenderTypes.entityAdditiveEmissive(TEX_NORMAL);
    private static final RenderType GLOW_ANCIENT = TCRenderTypes.entityAdditiveEmissive(TEX_ANCIENT);
    private static final RenderType GLOW_ELDRITCH = TCRenderTypes.entityAdditiveEmissive(TEX_ELDRITCH);
    private static final RenderType HALO_TYPE = TCRenderTypes.SPARKLE_CULLED;

    private final MatrixCubeModel model;
    private final RandomSource haloRandom = RandomSource.create();

    public InfusionMatrixRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new MatrixCubeModel(context.bakeLayer(TCModelLayers.MATRIX_CUBE));
    }

    @Override
    public void render(BlockEntityInfusionMatrix matrix, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int light, int overlay) {
        var viewEntity = Minecraft.getInstance().getCameraEntity();
        float animationTime = viewEntity == null ? partialTick : viewEntity.tickCount + partialTick;
        float startUp = matrix.clientStartUp;
        float stability = matrix.stability();
        int craftTicks = matrix.clientCraftTicks;
        boolean active = matrix.isActive();
        boolean crafting = matrix.isCrafting();
        boolean fancyGraphics = Minecraft.getInstance().options.graphicsMode().get() != GraphicsStatus.FAST;
        ResourceLocation texture = pickTexture(matrix);

        RenderType type = RenderType.entityCutout(texture);
        RenderType glowType = glowTypeFor(texture);
        float instability = Math.min(6.0F,
                1.0F + (stability < 0.0F ? -stability * 0.66F : 1.0F) * (Math.min(craftTicks, 50) / 50.0F));
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(animationTime % 360.0F * startUp));
        poseStack.mulPose(Axis.XP.rotationDegrees(TILT_X * startUp));
        poseStack.mulPose(Axis.ZP.rotationDegrees(TILT_Z * startUp));
        for (int a = 0; a < 2; a++) {
            for (int b = 0; b < 2; b++) {
                for (int c = 0; c < 2; c++) {
                    float jx = 0.0F;
                    float jy = 0.0F;
                    float jz = 0.0F;
                    if (active) {
                        jx = Mth.sin((animationTime + a * 10) / 15.0F) * JITTER_SCALE * startUp * instability;
                        jy = Mth.sin((animationTime + b * 10) / 14.0F) * JITTER_SCALE * startUp * instability;
                        jz = Mth.sin((animationTime + c * 10) / 13.0F) * JITTER_SCALE * startUp * instability;
                    }
                    int aa = a == 0 ? -1 : 1;
                    int bb = b == 0 ? -1 : 1;
                    int cc = c == 0 ? -1 : 1;
                    poseStack.pushPose();
                    poseStack.translate(jx + aa * SUB_CUBE_OFFSET, jy + bb * SUB_CUBE_OFFSET, jz + cc * SUB_CUBE_OFFSET);
                    if (a > 0) {
                        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    }
                    if (b > 0) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                    }
                    if (c > 0) {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                    }
                    poseStack.scale(SUB_CUBE_SCALE, SUB_CUBE_SCALE, SUB_CUBE_SCALE);
                    model.cube.render(poseStack, buffers.getBuffer(type), light, OverlayTexture.NO_OVERLAY, -1);
                    if (active) {
                        float glowAlpha = (Mth.sin((animationTime + a * 2 + b * 3 + c * 4) / 4.0F) * 0.1F + 0.2F)
                                * startUp;
                        model.glow.render(poseStack, buffers.getBuffer(glowType), LightTexture.FULL_BRIGHT,
                                OverlayTexture.NO_OVERLAY,
                                ARGB32.colorFromFloat(glowAlpha, GLOW_RED, GLOW_GREEN, GLOW_BLUE));
                    }
                    poseStack.popPose();
                }
            }
        }
        poseStack.popPose();
        if (crafting) {
            drawHalo(craftTicks, fancyGraphics, poseStack, buffers);
        }
    }

    private static ResourceLocation pickTexture(BlockEntityInfusionMatrix matrix) {
        Level level = matrix.getLevel();
        if (level == null) {
            return TEX_NORMAL;
        }
        BlockPos corner = matrix.getBlockPos().offset(-1, -2, -1);
        Block block = level.getBlockState(corner).getBlock();
        if (block == TCBlocks.PILLAR_ANCIENT.get()) {
            return TEX_ANCIENT;
        }
        if (block == TCBlocks.PILLAR_ELDRITCH.get()) {
            return TEX_ELDRITCH;
        }
        return TEX_NORMAL;
    }

    private static RenderType glowTypeFor(ResourceLocation texture) {
        if (texture.equals(TEX_ANCIENT)) {
            return GLOW_ANCIENT;
        }
        if (texture.equals(TEX_ELDRITCH)) {
            return GLOW_ELDRITCH;
        }
        return GLOW_NORMAL;
    }

    private void drawHalo(int craftTicks, boolean fancyGraphics, PoseStack poseStack, MultiBufferSource buffers) {
        int fans = fancyGraphics ? HALO_FANS_FANCY : HALO_FANS_FAST;
        float f1 = craftTicks / HALO_FADE_TICKS;
        float ramp = Math.min(craftTicks, HALO_RAMP_TICKS) / HALO_RAMP_TICKS;
        float centerAlpha = Math.max(0.0F, 1.0F - f1);
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        haloRandom.setSeed(HALO_SEED);
        VertexConsumer buffer = buffers.getBuffer(HALO_TYPE);
        for (int i = 0; i < fans; i++) {
            poseStack.mulPose(Axis.XP.rotationDegrees(haloRandom.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(haloRandom.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(haloRandom.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(haloRandom.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(haloRandom.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(haloRandom.nextFloat() * 360.0F + f1 * 360.0F));
            float fa = (haloRandom.nextFloat() * 20.0F + 5.0F) / 20.0F * ramp;
            float f4 = (haloRandom.nextFloat() * 2.0F + 1.0F) / 20.0F * ramp;
            Matrix4f mat = poseStack.last().pose();
            float bx1 = -0.866F * f4;
            float bz1 = -0.5F * f4;
            float bx2 = 0.866F * f4;
            float bz3 = f4;
            buffer.addVertex(mat, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, centerAlpha);
            buffer.addVertex(mat, bx1, fa, bz1).setColor(1.0F, 0.0F, 1.0F, 0.0F);
            buffer.addVertex(mat, bx2, fa, bz1).setColor(1.0F, 0.0F, 1.0F, 0.0F);
            buffer.addVertex(mat, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, centerAlpha);
            buffer.addVertex(mat, bx2, fa, bz1).setColor(1.0F, 0.0F, 1.0F, 0.0F);
            buffer.addVertex(mat, 0.0F, fa, bz3).setColor(1.0F, 0.0F, 1.0F, 0.0F);
            buffer.addVertex(mat, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, centerAlpha);
            buffer.addVertex(mat, 0.0F, fa, bz3).setColor(1.0F, 0.0F, 1.0F, 0.0F);
            buffer.addVertex(mat, bx1, fa, bz1).setColor(1.0F, 0.0F, 1.0F, 0.0F);
        }
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
