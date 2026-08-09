package com.leclowndu93150.thaumaturge.mixin.client.renderer.entity;

import com.leclowndu93150.thaumaturge.client.render.entity.TintBufferSource;
import com.leclowndu93150.thaumaturge.content.entity.champion.ChampionHelper;
import com.leclowndu93150.thaumaturge.content.entity.champion.ChampionModifier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<? super T>> {
    private static final int GRIM_TINT = ARGB32.colorFromFloat(1.0F, 0.6F, 0.6F, 0.6F);

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"), argsOnly = true)
    private MultiBufferSource thaumaturge$grimTint(MultiBufferSource buffers, T entity) {
        if (ChampionHelper.championType(entity) == ChampionModifier.GRIM) {
            return new TintBufferSource(buffers, GRIM_TINT);
        }
        return buffers;
    }
}
