package com.leclowndu93150.thaumcraft.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ResearchGate(ResourceLocation entry, Optional<Integer> stage, boolean negate) {
    public static final Codec<ResearchGate> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.fieldOf("entry").forGetter(ResearchGate::entry),
            Codec.INT.optionalFieldOf("stage").forGetter(ResearchGate::stage),
            Codec.BOOL.optionalFieldOf("negate", false).forGetter(ResearchGate::negate)
    ).apply(i, ResearchGate::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchGate> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ResearchGate::entry,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT),
            ResearchGate::stage,
            ByteBufCodecs.BOOL,
            ResearchGate::negate,
            ResearchGate::new
    );
}
