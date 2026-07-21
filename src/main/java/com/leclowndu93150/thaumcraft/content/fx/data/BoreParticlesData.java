package com.leclowndu93150.thaumcraft.content.fx.data;

import io.netty.buffer.ByteBuf;
import com.leclowndu93150.thaumcraft.registry.TCParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record BoreParticlesData(BlockState state, int targetEntityId, double tx, double ty, double tz,
                                double sx, double sy, double sz) implements ParticleOptions {
    public static final int NO_ENTITY = -1;
    private static final Codec<BlockState> BLOCK_STATE_CODEC = Codec.withAlternative(
            BlockState.CODEC, BuiltInRegistries.BLOCK.byNameCodec(), Block::defaultBlockState);

    public static final MapCodec<BoreParticlesData> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BLOCK_STATE_CODEC.fieldOf("block_state").forGetter(BoreParticlesData::state),
            Codec.INT.fieldOf("target_entity_id").forGetter(BoreParticlesData::targetEntityId),
            Codec.DOUBLE.fieldOf("tx").forGetter(BoreParticlesData::tx),
            Codec.DOUBLE.fieldOf("ty").forGetter(BoreParticlesData::ty),
            Codec.DOUBLE.fieldOf("tz").forGetter(BoreParticlesData::tz),
            Codec.DOUBLE.fieldOf("sx").forGetter(BoreParticlesData::sx),
            Codec.DOUBLE.fieldOf("sy").forGetter(BoreParticlesData::sy),
            Codec.DOUBLE.fieldOf("sz").forGetter(BoreParticlesData::sz)
    ).apply(inst, BoreParticlesData::new));

    private static final StreamCodec<ByteBuf, BlockState> STATE_STREAM_CODEC =
            ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);

    public static final StreamCodec<RegistryFriendlyByteBuf, BoreParticlesData> STREAM_CODEC = StreamCodec.of(
            (buffer, data) -> {
                STATE_STREAM_CODEC.encode(buffer, data.state());
                buffer.writeVarInt(data.targetEntityId());
                buffer.writeDouble(data.tx());
                buffer.writeDouble(data.ty());
                buffer.writeDouble(data.tz());
                buffer.writeDouble(data.sx());
                buffer.writeDouble(data.sy());
                buffer.writeDouble(data.sz());
            },
            buffer -> new BoreParticlesData(STATE_STREAM_CODEC.decode(buffer), buffer.readVarInt(),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));

    public BoreParticlesData(BlockState state, double tx, double ty, double tz, double sx, double sy, double sz) {
        this(state, NO_ENTITY, tx, ty, tz, sx, sy, sz);
    }

    @Override
    public ParticleType<?> getType() {
        return TCParticles.BORE_PARTICLES.get();
    }
}
