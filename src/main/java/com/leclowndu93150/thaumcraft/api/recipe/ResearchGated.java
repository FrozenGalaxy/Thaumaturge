package com.leclowndu93150.thaumcraft.api.recipe;

import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface ResearchGated {

    Optional<ResearchGate> researchGate();

    default boolean doesPassGate(Player player){
        return ResearchGate.passes(player, researchGate().orElse(null));
    }
}
