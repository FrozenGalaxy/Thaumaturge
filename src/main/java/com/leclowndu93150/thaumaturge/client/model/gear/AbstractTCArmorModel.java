package com.leclowndu93150.thaumaturge.client.model.gear;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public abstract class AbstractTCArmorModel extends HumanoidModel<LivingEntity> {
    protected AbstractTCArmorModel(ModelPart root) {
        super(root);
    }
}
