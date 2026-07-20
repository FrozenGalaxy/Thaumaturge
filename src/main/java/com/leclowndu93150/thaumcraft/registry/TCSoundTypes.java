package com.leclowndu93150.thaumcraft.registry;

import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public final class TCSoundTypes {
    public static final SoundType GORE = new DeferredSoundType(
            0.5F, 1.0F,
            TCSounds.GORE::value,
            TCSounds.GORE::value,
            TCSounds.GORE::value,
            TCSounds.GORE::value,
            TCSounds.GORE::value
    );

    public static final SoundType CRYSTAL = new DeferredSoundType(
            0.5F, 1.0F,
            TCSounds.CRYSTAL::value,
            TCSounds.CRYSTAL::value,
            TCSounds.CRYSTAL::value,
            TCSounds.CRYSTAL::value,
            TCSounds.CRYSTAL::value
    );

    public static final SoundType JAR = new DeferredSoundType(
            0.5F, 1.0F,
            TCSounds.JAR::value,
            TCSounds.JAR::value,
            TCSounds.JAR::value,
            TCSounds.JAR::value,
            TCSounds.JAR::value
    );

    public static final SoundType URN = new DeferredSoundType(
            0.5F, 1.5F,
            TCSounds.URNBREAK::value,
            TCSounds.URNBREAK::value,
            TCSounds.URNBREAK::value,
            TCSounds.URNBREAK::value,
            TCSounds.URNBREAK::value
    );

    private TCSoundTypes() {}
}
