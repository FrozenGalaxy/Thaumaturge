package com.leclowndu93150.thaumcraft.content.wands;

import com.leclowndu93150.thaumcraft.api.wands.WandCap;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class ItemWandCap extends Item {
    private final Supplier<WandCap> cap;

    public ItemWandCap(Item.Properties properties, Supplier<WandCap> cap) {
        super(properties);
        this.cap = cap;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> builder, TooltipFlag flag) {
        builder.add(WandTooltips.capCostSummary(context.registries(), cap.get()));
    }
}
