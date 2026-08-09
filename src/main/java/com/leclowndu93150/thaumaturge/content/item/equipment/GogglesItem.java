package com.leclowndu93150.thaumaturge.content.item.equipment;

import com.leclowndu93150.thaumaturge.api.items.IGoggles;
import com.leclowndu93150.thaumaturge.api.items.IRevealer;
import com.leclowndu93150.thaumaturge.api.items.IVisDiscountGear;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class GogglesItem extends Item implements IGoggles, IRevealer, IVisDiscountGear, Equipable {
    public GogglesItem(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public boolean showIngamePopups(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Override
    public boolean showNodes(ItemStack stack, LivingEntity holder) {
        return true;
    }

    @Override
    public int getVisDiscount(ItemStack stack) {
        return 5;
    }

}
