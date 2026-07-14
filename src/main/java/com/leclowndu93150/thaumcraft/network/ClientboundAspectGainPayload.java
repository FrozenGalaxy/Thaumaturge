package com.leclowndu93150.thaumcraft.network;

import com.leclowndu93150.thaumcraft.TCIds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundAspectGainPayload(ResourceLocation aspect, int amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundAspectGainPayload> TYPE =
            new CustomPacketPayload.Type<>(TCIds.rl("aspect_gain"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAspectGainPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, ClientboundAspectGainPayload::aspect,
                    ByteBufCodecs.VAR_INT, ClientboundAspectGainPayload::amount,
                    ClientboundAspectGainPayload::new);

    @Override
    public CustomPacketPayload.Type<ClientboundAspectGainPayload> type() {
        return TYPE;
    }
}
