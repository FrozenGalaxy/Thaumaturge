package com.leclowndu93150.thaumcraft.content.research.table;

import com.leclowndu93150.thaumcraft.registry.TCBlocks;
import com.leclowndu93150.thaumcraft.registry.TCMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public final class MenuResearchTable extends AbstractContainerMenu {
    public static final int SCRIBE_TOOLS_X = 16;
    public static final int SCRIBE_TOOLS_Y = 15;
    public static final int PAPER_X = 224;
    public static final int PAPER_Y = 16;

    public static final int PLAYER_GRID_X = 77;
    public static final int PLAYER_GRID_Y = 190;
    public static final int CRAFTING_GRID_X = 20;
    public static final int CRAFTING_GRID_Y = 190;

    public static final int TABLE_SLOT_COUNT = BlockEntityResearchTable.SLOT_COUNT;
    public static final int PLAYER_ROW_SLOTS = 9;
    public static final int PLAYER_ROWS = 3;
    public static final int PLAYER_TOTAL_SLOTS = PLAYER_ROW_SLOTS * (PLAYER_ROWS + 1);
    public static final int TOTAL_INVENTORY_SLOTS = TABLE_SLOT_COUNT + PLAYER_TOTAL_SLOTS;

    private final ItemStackHandler items;
    private final ContainerLevelAccess access;
    private final BlockPos pos;

    public MenuResearchTable(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(BlockEntityResearchTable.SLOT_COUNT), ContainerLevelAccess.NULL, buf.readBlockPos());
    }

    public MenuResearchTable(int containerId, Inventory playerInventory, BlockEntityResearchTable blockEntity) {
        this(containerId, playerInventory, blockEntity.items(),
                ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                blockEntity.getBlockPos());
    }

    private MenuResearchTable(int containerId, Inventory playerInventory, ItemStackHandler items, ContainerLevelAccess access, BlockPos pos) {
        super(TCMenus.RESEARCH_TABLE.get(), containerId);
        this.items = items;
        this.access = access;
        this.pos = pos;

        addSlot(new SlotItemHandler(items, BlockEntityResearchTable.SLOT_SCRIBE_TOOLS, SCRIBE_TOOLS_X, SCRIBE_TOOLS_Y));
        addSlot(new SlotItemHandler(items, BlockEntityResearchTable.SLOT_PAPER, PAPER_X, PAPER_Y));

        for (int row = 0; row < PLAYER_ROWS; row++) {
            for (int col = 0; col < PLAYER_ROW_SLOTS; col++) {
                addSlot(new Slot(playerInventory, col + row * PLAYER_ROW_SLOTS + PLAYER_ROW_SLOTS,
                        PLAYER_GRID_X + col * 18, PLAYER_GRID_Y + row * 18));
            }
        }

        for (int row = 0; row < PLAYER_ROWS; row++) {
            for (int col = 0; col < PLAYER_ROWS; col++) {
                addSlot(new Slot(playerInventory, col + row * PLAYER_ROWS,
                        CRAFTING_GRID_X + col * 18, PLAYER_GRID_Y + row * 18));
            }
        }
    }

    public BlockPos pos() {
        return pos;
    }

    public ItemStackHandler tableItems() {
        return items;
    }

    public BlockPos tablePos() {
        return pos;
    }

    public boolean hasUsableScribeTools() {
        ItemStack resource = items.getStackInSlot(BlockEntityResearchTable.SLOT_SCRIBE_TOOLS);
        int amount = items.getStackInSlot(BlockEntityResearchTable.SLOT_SCRIBE_TOOLS).getCount();
        if (resource.isEmpty() || amount <= 0) return false;
        ItemStack stack = resource.copyWithCount(amount);
        return stack.isDamageableItem() && stack.getDamageValue() < stack.getMaxDamage();
    }

    public boolean hasPaperReady() {
        ItemStack resource = items.getStackInSlot(BlockEntityResearchTable.SLOT_PAPER);
        int amount = items.getStackInSlot(BlockEntityResearchTable.SLOT_PAPER).getCount();
        return !resource.isEmpty() && resource.getItem() == Items.PAPER && amount > 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(access, player, TCBlocks.RESEARCH_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack returnStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            returnStack = stackInSlot.copy();
            if (slotIndex < TABLE_SLOT_COUNT) {
                if (!moveItemStackTo(stackInSlot, TABLE_SLOT_COUNT, TOTAL_INVENTORY_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, TABLE_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return returnStack;
    }
}
