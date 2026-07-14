package com.leclowndu93150.thaumcraft.content.item;

import com.leclowndu93150.thaumcraft.api.aura.AuraHelper;
import com.leclowndu93150.thaumcraft.api.capability.KnowledgeAccess;
import com.leclowndu93150.thaumcraft.api.research.TCResearchEntries;
import com.leclowndu93150.thaumcraft.api.research.scan.ScanningManager;
import com.leclowndu93150.thaumcraft.content.research.ResearchManager;
import com.leclowndu93150.thaumcraft.content.research.scan.PointedEntityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public final class ThaumometerItem extends Item {
    public static final double SCAN_ENTITY_MIN_RANGE = 1.0;
    public static final double SCAN_ENTITY_RANGE = 9.0;
    public static final int USE_DURATION_TICKS = 25;
    public static final int SCAN_COMPLETE_ELAPSED_TICKS = 20;

    private static final int SCAN_RELEASE_TOLERANCE_TICKS = 18;
    private static final int AURA_CHECK_INTERVAL_TICKS = 20;
    private static final int FLUX_WARN_BASE_DIVISOR = 3;

    public ThaumometerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!ScanningManager.isThingStillScannable(player, resolveTarget(level, player))) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (level.isClientSide() || !(entity instanceof Player)) {
            return;
        }
        if (USE_DURATION_TICKS - remaining >= SCAN_COMPLETE_ELAPSED_TICKS) {
            entity.releaseUsingItem();
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (level.isClientSide() || !(entity instanceof Player player)) {
            return;
        }
        if (USE_DURATION_TICKS - remaining < SCAN_RELEASE_TOLERANCE_TICKS) {
            return;
        }
        Object target = resolveTarget(level, player);
        if (!ScanningManager.isThingStillScannable(player, target)) {
            return;
        }
        ScanningManager.scanTheThing(player, target);
        return;
    }

    public static @Nullable Object resolveTarget(Level level, Player player) {
        Entity target = PointedEntityHelper.getPointedEntity(
                level, player, SCAN_ENTITY_MIN_RANGE, SCAN_ENTITY_RANGE, 0.0F, true);
        if (target != null) {
            return target;
        }
        BlockHitResult hit = level.clip(new ClipContext(
                player.getEyePosition(),
                player.getEyePosition().add(player.getLookAngle().scale(player.blockInteractionRange())),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
        return hit.getType() == HitResult.Type.BLOCK ? hit.getBlockPos() : null;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level levelIn, Entity owner, int slotId, boolean isSelected) {
        if (!(levelIn instanceof ServerLevel level)) {
            return;
        }
        boolean held = isSelected || (owner instanceof LivingEntity living && living.getOffhandItem() == stack);
        if (held && owner.tickCount % AURA_CHECK_INTERVAL_TICKS == 0 && owner instanceof ServerPlayer player) {
            warnAboutFlux(level, player);
        }
    }

    private static void warnAboutFlux(ServerLevel level, ServerPlayer player) {
        BlockPos pos = player.blockPosition();
        float flux = AuraHelper.getFlux(level, pos);
        boolean dangerous = flux > AuraHelper.getVis(level, pos)
                || flux > AuraHelper.getAuraBase(level, pos) / (float) FLUX_WARN_BASE_DIVISOR;
        if (dangerous && !KnowledgeAccess.of(player).isResearchKnown(TCResearchEntries.FLUX)) {
            ResearchManager.complete(player, TCResearchEntries.FLUX);
            player.displayClientMessage(Component.translatable("research.thaumcraft.flux.warn")
                    .withStyle(ChatFormatting.DARK_PURPLE), true);
        }
    }
}
