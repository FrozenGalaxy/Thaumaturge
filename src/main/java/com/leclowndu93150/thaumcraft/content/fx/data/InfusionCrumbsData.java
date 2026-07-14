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
import net.minecraft.world.item.ItemStack;

public record InfusionCrumbsData(ItemStack stack, double tx, double ty, double tz,
                                 double sx, double sy, double sz) implements ParticleOptions {
    public static final MapCodec<InfusionCrumbsData> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(InfusionCrumbsData::stack),
            Codec.DOUBLE.fieldOf("tx").forGetter(InfusionCrumbsData::tx),
            Codec.DOUBLE.fieldOf("ty").forGetter(InfusionCrumbsData::ty),
            Codec.DOUBLE.fieldOf("tz").forGetter(InfusionCrumbsData::tz),
            Codec.DOUBLE.fieldOf("sx").forGetter(InfusionCrumbsData::sx),
            Codec.DOUBLE.fieldOf("sy").forGetter(InfusionCrumbsData::sy),
            Codec.DOUBLE.fieldOf("sz").forGetter(InfusionCrumbsData::sz)
    ).apply(inst, InfusionCrumbsData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, InfusionCrumbsData> STREAM_CODEC = StreamCodec.of(
            (buffer, data) -> {
                ItemStack.STREAM_CODEC.encode(buffer, data.stack());
                buffer.writeDouble(data.tx());
                buffer.writeDouble(data.ty());
                buffer.writeDouble(data.tz());
                buffer.writeDouble(data.sx());
                buffer.writeDouble(data.sy());
                buffer.writeDouble(data.sz());
            },
            buffer -> new InfusionCrumbsData(ItemStack.STREAM_CODEC.decode(buffer),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));

    @Override
    public ParticleType<?> getType() {
        return TCParticles.INFUSION_CRUMBS.get();
    }
}
