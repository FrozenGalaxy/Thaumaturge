package com.leclowndu93150.thaumcraft.client.golem;

import com.leclowndu93150.thaumcraft.content.golem.GolemProperties;
import net.minecraft.world.item.ItemStack;

public final class GolemRenderState {
    public GolemProperties props;
    public byte color;
    public float ageInTicks;
    public int lightCoords;
    public float bodyRot;
    public float headYawDelta;
    public float pitch;
    public float walkPos;
    public float walkSpeed;
    public float attackTime;
    public double speedSq;
    public float yawDelta;
    public float wheelRotation;
    public float grinderRot;
    public boolean combat;
    public boolean invisible;
    public boolean ghost;
    public boolean xray;
    public ItemStack heldItem = ItemStack.EMPTY;
    public boolean holdingItem;
    public ItemStack haulerItem = ItemStack.EMPTY;
    public boolean haulingItem;
    public boolean heldItemIsBlock;
    public boolean haulerItemIsBlock;
    public String accessories = "";
}
