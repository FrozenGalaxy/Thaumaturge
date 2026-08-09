package com.leclowndu93150.thaumaturge.content.wands;

import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class ItemWandRod extends Item {
    private final Supplier<WandRod> rod;

    public ItemWandRod(Item.Properties properties, Supplier<WandRod> rod) {
        super(properties);
        this.rod = rod;
    }

    public WandRod rod() {
        return rod.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> builder, TooltipFlag flag) {
        builder.add(Component.translatable("tooltip.thaumaturge.wand.capacity", rod.get().capacity())
                .withStyle(ChatFormatting.GOLD));
    }
}
