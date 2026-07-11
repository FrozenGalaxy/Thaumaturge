package com.leclowndu93150.thaumcraft.content.wands;

import com.leclowndu93150.thaumcraft.api.aspect.IAspect;
import com.leclowndu93150.thaumcraft.api.aspect.TCAspects;
import com.leclowndu93150.thaumcraft.api.aura.AuraHelper;
import com.leclowndu93150.thaumcraft.api.wands.IWandRodOnUpdate;
import com.leclowndu93150.thaumcraft.api.wands.WandCap;
import com.leclowndu93150.thaumcraft.api.wands.WandRod;
import com.leclowndu93150.thaumcraft.content.world.crystal.BlockCrystal;
import com.leclowndu93150.thaumcraft.registry.TCDataComponents;
import com.leclowndu93150.thaumcraft.registry.TCWandParts;
import java.text.DecimalFormat;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public class ItemWand extends Item {
    private static final DecimalFormat VIS_FORMAT = new DecimalFormat("#######.##");
    private static final String STAFF_ROD_SUFFIX = "_staff";

    public ItemWand(Properties properties) {
        super(properties);
    }

    public static ItemStack create(Item wandItem, WandCap cap, WandRod rod, boolean sceptre) {
        ItemStack stack = new ItemStack(wandItem);
        stack.set(TCDataComponents.WAND_PARTS.get(), new WandParts(cap, rod, sceptre));
        return stack;
    }

    public WandParts getParts(ItemStack stack) {
        return WandVisHelper.getParts(stack);
    }

    public boolean isStaff(ItemStack stack) {
        return getParts(stack).rod().staff();
    }

    public boolean isSceptre(ItemStack stack) {
        return getParts(stack).sceptre();
    }

    public boolean hasRunes(ItemStack stack) {
        return getParts(stack).rod().runes();
    }

    @Override
    public Component getName(ItemStack stack) {
        WandParts parts = getParts(stack);
        String capName = TCWandParts.caps().getKey(parts.cap()).getPath();
        String rodName = TCWandParts.rods().getKey(parts.rod()).getPath();
        if (rodName.endsWith(STAFF_ROD_SUFFIX)) {
            rodName = rodName.substring(0, rodName.length() - STAFF_ROD_SUFFIX.length());
        }
        String objKey = parts.rod().staff() ? "item.thaumcraft.wand.staff"
                : parts.sceptre() ? "item.thaumcraft.wand.sceptre"
                : "item.thaumcraft.wand.named";
        return Component.translatable(objKey,
                Component.translatable("wand.thaumcraft.cap." + capName),
                Component.translatable("wand.thaumcraft.rod." + rodName));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticksRemaining) {
        if (!(level instanceof ServerLevel) || !(entity instanceof Player player)) {
            return;
        }
        if (ticksRemaining % WandEconomy.CRUDE_REFINE_INTERVAL_TICKS != 0) {
            return;
        }
        ResourceKey<IAspect> target = refineTarget(player, stack);
        if (target == null) {
            return;
        }
        int room = WandVisHelper.getMaxVis(stack) - WandVisHelper.getVis(stack, target);
        int gain = Math.min(WandEconomy.CRUDE_REFINE_CENTIVIS_PER_OP, room);
        if (gain <= 0) {
            return;
        }
        float rawCost = gain * WandEconomy.RAW_TO_PRIMAL_RATIO / (float) WandEconomy.CENTIVIS_PER_VIS;
        float drained = AuraHelper.drainVis(level, player.blockPosition(), rawCost, false);
        int gained = (int) (drained * WandEconomy.CENTIVIS_PER_VIS / WandEconomy.RAW_TO_PRIMAL_RATIO);
        if (gained > 0) {
            WandVisHelper.addRealVis(stack, target, gained, true);
        }
    }

    private @Nullable ResourceKey<IAspect> refineTarget(Player player, ItemStack stack) {
        HitResult hit = player.pick(WandEconomy.CRUDE_REFINE_TARGET_RANGE, 0.0F, false);
        if (hit instanceof BlockHitResult blockHit
                && player.level().getBlockState(blockHit.getBlockPos()).getBlock() instanceof BlockCrystal crystal
                && !crystal.isFlux()
                && crystal.aspect() != null
                && TCAspects.PRIMALS.contains(crystal.aspect())) {
            return crystal.aspect();
        }
        WandVis vis = WandVisHelper.getAllVis(stack);
        int max = WandVisHelper.getMaxVis(stack);
        ResourceKey<IAspect> lowest = null;
        int lowestAmount = Integer.MAX_VALUE;
        for (ResourceKey<IAspect> primal : TCAspects.PRIMALS) {
            int amount = vis.amount(primal);
            if (amount < max && amount < lowestAmount) {
                lowestAmount = amount;
                lowest = primal;
            }
        }
        return lowest;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (entity instanceof Player player) {
            IWandRodOnUpdate onUpdate = getParts(stack).rod().onUpdate();
            if (onUpdate != null) {
                onUpdate.onUpdate(stack, player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("tooltip.thaumcraft.wand.capacity",
                        WandVisHelper.getMaxVis(stack) / WandEconomy.CENTIVIS_PER_VIS)
                .withStyle(ChatFormatting.GOLD));
        WandVis vis = WandVisHelper.getAllVis(stack);
        if (vis.isEmpty()) {
            return;
        }
        HolderLookup.Provider registries = context.registries();
        for (ResourceKey<IAspect> primal : TCAspects.PRIMALS) {
            int amount = vis.amount(primal);
            if (amount <= 0) {
                continue;
            }
            float modifier = WandVisHelper.getConsumptionModifier(stack, null, primal, false);
            Component name = Component.translatable("aspect.thaumcraft." + primal.identifier().getPath())
                    .withStyle(primalColor(registries, primal));
            builder.accept(Component.translatable("tooltip.thaumcraft.wand.vis",
                    name,
                    VIS_FORMAT.format(amount / (float) WandEconomy.CENTIVIS_PER_VIS),
                    VIS_FORMAT.format(modifier * 100.0F)));
        }
    }

    private static ChatFormatting primalColor(HolderLookup.@Nullable Provider registries,
            ResourceKey<IAspect> primal) {
        if (registries != null) {
            ChatFormatting color = registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(primal)
                    .value().chatColor()
                    .map(code -> ChatFormatting.getByCode(code.charAt(0)))
                    .orElse(null);
            if (color != null) {
                return color;
            }
        }
        return ChatFormatting.GRAY;
    }
}
