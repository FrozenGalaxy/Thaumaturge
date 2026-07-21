package com.leclowndu93150.thaumcraft.content.fx.data;

import com.leclowndu93150.thaumcraft.registry.TCParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BoreSparkleData(int targetEntityId, double tx, double ty, double tz,
                              float r, float g, float b) implements ParticleOptions {
    public static final int NO_ENTITY = -1;

    public static final MapCodec<BoreSparkleData> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.fieldOf("target_entity_id").forGetter(BoreSparkleData::targetEntityId),
            Codec.DOUBLE.fieldOf("tx").forGetter(BoreSparkleData::tx),
            Codec.DOUBLE.fieldOf("ty").forGetter(BoreSparkleData::ty),
            Codec.DOUBLE.fieldOf("tz").forGetter(BoreSparkleData::tz),
            Codec.FLOAT.fieldOf("r").forGetter(BoreSparkleData::r),
            Codec.FLOAT.fieldOf("g").forGetter(BoreSparkleData::g),
            Codec.FLOAT.fieldOf("b").forGetter(BoreSparkleData::b)
    ).apply(inst, BoreSparkleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BoreSparkleData> STREAM_CODEC = StreamCodec.of(
            (buffer, data) -> {
                buffer.writeVarInt(data.targetEntityId());
                buffer.writeDouble(data.tx());
                buffer.writeDouble(data.ty());
                buffer.writeDouble(data.tz());
                buffer.writeFloat(data.r());
                buffer.writeFloat(data.g());
                buffer.writeFloat(data.b());
            },
            buffer -> new BoreSparkleData(buffer.readVarInt(),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));

    public BoreSparkleData(double tx, double ty, double tz, float r, float g, float b) {
        this(NO_ENTITY, tx, ty, tz, r, g, b);
    }

    @Override
    public ParticleType<?> getType() {
        return TCParticles.BORE_SPARKLE.get();
    }
}
