package com.leclowndu93150.thaumcraft.api.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

/**
 * An extra page attached to a research entry that only becomes visible once the entry itself is
 * complete and every research listed in {@link #requiredResearch()} is complete.
 *
 * @param textKey translation key for the page body text
 * @param recipes recipe identifiers displayed on the page
 * @param requiredResearch research entries that must be complete for the page to show
 * @since 1.0.0
 */
public record ResearchAddendum(String textKey, List<ResourceLocation> recipes, List<ResourceLocation> requiredResearch) {
    /** Codec for datapack serialization. */
    public static final Codec<ResearchAddendum> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("text").forGetter(ResearchAddendum::textKey),
            ResourceLocation.CODEC.listOf().optionalFieldOf("recipes", List.of()).forGetter(ResearchAddendum::recipes),
            ResourceLocation.CODEC.listOf().optionalFieldOf("required_research", List.of()).forGetter(ResearchAddendum::requiredResearch)
    ).apply(instance, ResearchAddendum::new));
}
