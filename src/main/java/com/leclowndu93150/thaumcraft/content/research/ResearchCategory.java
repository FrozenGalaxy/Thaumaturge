package com.leclowndu93150.thaumcraft.content.research;

import com.leclowndu93150.thaumcraft.api.aspect.AspectList;
import com.leclowndu93150.thaumcraft.api.research.IResearchCategory;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public record ResearchCategory(
        Optional<ResourceLocation> requiredResearch,
        AspectList formula,
        ResourceLocation icon,
        ResourceLocation background,
        Optional<ResourceLocation> overlayBackground
) implements IResearchCategory {
    public static final Codec<ResearchCategory> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("required_research").forGetter(ResearchCategory::requiredResearch),
            AspectList.CODEC.fieldOf("formula").forGetter(ResearchCategory::formula),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(ResearchCategory::icon),
            ResourceLocation.CODEC.fieldOf("background").forGetter(ResearchCategory::background),
            ResourceLocation.CODEC.optionalFieldOf("overlay_background").forGetter(ResearchCategory::overlayBackground)
    ).apply(instance, ResearchCategory::new));

    public static final Codec<IResearchCategory> CODEC = DIRECT_CODEC.xmap(c -> (IResearchCategory) c, ResearchCategory::ofInterface);

    private static ResearchCategory ofInterface(IResearchCategory category) {
        if (category instanceof ResearchCategory concrete) {
            return concrete;
        }
        return new ResearchCategory(
                category.requiredResearch(),
                category.formula(),
                category.icon(),
                category.background(),
                category.overlayBackground()
        );
    }
}
