package com.leclowndu93150.thaumcraft.api.casters;

import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;

/**
 * Registration entry for a focus element: how to create fresh instances plus the icon and
 * color used to draw the element in the focal manipulator.
 *
 * {@code thaumcraft.api.casters.FocusEngine} (class, icon, color) with a single registered
 * value. Register instances under {@link #REGISTRY_KEY}; {@link FocusEngine} resolves elements
 * from that registry once it is bound.
 *
 * @param factory creates a fresh, unconfigured element instance per lookup; instances are
 *                mutable and never shared
 * @param icon    the texture drawn for this element in casting UIs
 * @param color   the packed {@code 0xRRGGBB} tint applied to the icon
 * @since 1.0.0
 */
public record FocusElementType(Supplier<? extends IFocusElement> factory, ResourceLocation icon, int color) {
    /** The registry key for focus element types. */
    public static final ResourceKey<Registry<FocusElementType>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath("thaumcraft", "focus_element"));
}
