package com.leclowndu93150.thaumaturge.api.essentia;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;

/**
 * Side-agnostic synthetic aspect view exposed by tubes and filters whose aspect content is
 * a routing intent rather than physical storage.
 *
 * <p>Exposed through {@link EssentiaCapabilities#ASPECT_QUERY}.
 *
 * @since 1.0.0
 */
public interface IAspectQuery {
    /**
     * The aspect intent advertised by this block, or {@link AspectList#EMPTY} when none.
     *
     * <p>The amount on this surface signals an intent rather than a quantity. {@link AspectList}
     * forbids non-positive amounts, so the entry is reported as {@code 1}. Consumers must treat
     * amounts on this surface as untyped markers and never as a storage quantity.
     *
     * @return the advertised aspects, never {@code null}
     */
    AspectList queryAspects();
}
