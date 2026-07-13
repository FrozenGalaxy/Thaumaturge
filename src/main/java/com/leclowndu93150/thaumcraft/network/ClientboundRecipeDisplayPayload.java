package com.leclowndu93150.thaumcraft.network;

import com.leclowndu93150.thaumcraft.TCIds;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

public record ClientboundRecipeDisplayPayload(ResourceLocation recipeId, List<RecipeDisplay> displays) implements CustomPacketPayload {
    public static final Type<ClientboundRecipeDisplayPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "recipe_display"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRecipeDisplayPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    ClientboundRecipeDisplayPayload::recipeId,
                    RecipeDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    ClientboundRecipeDisplayPayload::displays,
                    ClientboundRecipeDisplayPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
