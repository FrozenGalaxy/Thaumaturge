package com.leclowndu93150.thaumcraft.content.world.objects;

import com.leclowndu93150.thaumcraft.config.ThaumcraftCommonConfig;
import com.leclowndu93150.thaumcraft.registry.TCPlacementModifiers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public final class ConfigRarityFilter extends PlacementFilter {
    public static final ConfigRarityFilter CRIMSON_PORTAL = new ConfigRarityFilter();
    public static final MapCodec<ConfigRarityFilter> CODEC = MapCodec.unit(CRIMSON_PORTAL);

    private ConfigRarityFilter() {}

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        int rarity = ThaumcraftCommonConfig.CRIMSON_PORTAL_RARITY.get();
        return rarity > 0 && random.nextInt(rarity) == 0;
    }

    @Override
    public PlacementModifierType<?> type() {
        return TCPlacementModifiers.CRIMSON_PORTAL_RARITY.get();
    }
}
