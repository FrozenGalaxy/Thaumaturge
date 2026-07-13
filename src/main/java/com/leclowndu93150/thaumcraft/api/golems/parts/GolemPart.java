package com.leclowndu93150.thaumcraft.api.golems.parts;

import com.leclowndu93150.thaumcraft.api.golems.GolemTrait;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

/**
 * Base description shared by every golem part kind: the research gating it, the icon shown
 * in the golem press, the crafting components it consumes, the traits it grants and the
 * model it renders with.
 *
 * @since 1.0.0
 */
public abstract class GolemPart {
    private final List<ResourceLocation> research;
    private final ResourceLocation icon;
    private final List<GolemComponent> components;
    private final List<GolemTrait> traits;
    private final GolemPartModel model;

    protected GolemPart(List<ResourceLocation> research, ResourceLocation icon, List<GolemComponent> components,
                        List<GolemTrait> traits, @Nullable GolemPartModel model) {
        this.research = List.copyOf(research);
        this.icon = icon;
        this.components = List.copyOf(components);
        this.traits = List.copyOf(traits);
        this.model = model;
    }

    /**
     * @return research entries gating this part; empty means ungated
     */
    public List<ResourceLocation> research() {
        return research;
    }

    /**
     * @return the icon drawn for this part in the golem press
     */
    public ResourceLocation icon() {
        return icon;
    }

    /**
     * @return the crafting components consumed by this part
     */
    public List<GolemComponent> components() {
        return components;
    }

    /**
     * @return traits granted by this part
     */
    public List<GolemTrait> traits() {
        return traits;
    }

    /**
     * @return the model rendered for this part, or null when it has no visual
     */
    public @Nullable GolemPartModel model() {
        return model;
    }

    /**
     * @return the behavior ticked for this part, or null when it has none
     */
    public abstract @Nullable IGolemFunction function();
}
