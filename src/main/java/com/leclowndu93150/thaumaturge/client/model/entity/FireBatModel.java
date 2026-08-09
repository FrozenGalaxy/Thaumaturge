package com.leclowndu93150.thaumaturge.client.model.entity;

import com.leclowndu93150.thaumaturge.content.entity.IBatAnimated;
import net.minecraft.client.animation.definitions.BatAnimation;
import net.minecraft.client.model.BatModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import java.util.function.Function;

public final class FireBatModel<T extends Mob & IBatAnimated> extends HierarchicalModel<T> {
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;

    private final ModelPart root;
    private final ModelPart head;

    public FireBatModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
    }

    public FireBatModel(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
        this.root = root;
        this.head = root.getChild("head");
    }

    public static LayerDefinition createLayer() {
        return BatModel.createBodyLayer();
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity.isResting()) {
            this.head.yRot = netHeadYaw * DEG_TO_RAD;
        }
        this.animate(entity.flyAnimation(), BatAnimation.BAT_FLYING, ageInTicks, 1.0F);
        this.animate(entity.restAnimation(), BatAnimation.BAT_RESTING, ageInTicks, 1.0F);
    }
}
