package com.leclowndu93150.thaumaturge.api.casters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

/**
 * A ray along which a spell travels: an origin point and a normalized direction.
 *
 * @param source    the world-space origin of the ray
 * @param direction the direction of travel; suppliers normalize it
 * @since 1.0.0
 */
public record Trajectory(Vec3 source, Vec3 direction) {
    /** Serializes both vectors as double triples. */
    public static final Codec<Trajectory> CODEC = RecordCodecBuilder.create(i -> i.group(
            Vec3.CODEC.fieldOf("source").forGetter(Trajectory::source),
            Vec3.CODEC.fieldOf("direction").forGetter(Trajectory::direction)
    ).apply(i, Trajectory::new));

    /** Network encoding of a single vector as double triples. */
    private static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, Vec3::x,
            ByteBufCodecs.DOUBLE, Vec3::y,
            ByteBufCodecs.DOUBLE, Vec3::z,
            Vec3::new
    );

    /** Network encoding mirroring {@link #CODEC}. */
    public static final StreamCodec<ByteBuf, Trajectory> STREAM_CODEC = StreamCodec.composite(
            VEC3_STREAM_CODEC,
            Trajectory::source,
            VEC3_STREAM_CODEC,
            Trajectory::direction,
            Trajectory::new
    );
}
