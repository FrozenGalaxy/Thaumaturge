package com.leclowndu93150.thaumcraft.client.render.research;

import com.leclowndu93150.thaumcraft.api.aspect.AspectInstance;
import com.leclowndu93150.thaumcraft.api.recipe.IArcaneRecipe;
import com.leclowndu93150.thaumcraft.client.render.GuiBlend;
import com.leclowndu93150.thaumcraft.client.screen.TCScreenTextures;
import com.leclowndu93150.thaumcraft.content.recipe.workbench.ArcaneShapedCraftingRecipe;
import com.leclowndu93150.thaumcraft.content.recipe.workbench.ArcaneShapelessCraftingRecipe;
import com.leclowndu93150.thaumcraft.content.taint.item.EssentiaCrystalFactory;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jspecify.annotations.Nullable;

public final class RecipeDisplayWidget {
    public static final int PANEL_SIZE = 104;
    public static final int CENTER_OFFSET = 52;

    private static final int WORKBENCH_PANEL_U = 60;
    private static final int WORKBENCH_PANEL_V = 15;
    private static final int WORKBENCH_PANEL_W = 51;
    private static final int WORKBENCH_PANEL_H = 52;
    private static final int WORKBENCH_PANEL_OFFSET_X = -26;
    private static final int WORKBENCH_PANEL_OFFSET_Y = -26;

    private static final int ARCANE_PANEL_U = 112;
    private static final int ARCANE_PANEL_V = 15;
    private static final int ARCANE_PANEL_W = 52;
    private static final int ARCANE_PANEL_H = 52;
    private static final int ARCANE_PANEL_OFFSET_X = -26;
    private static final int ARCANE_PANEL_OFFSET_Y = -26;

    private static final int SLOT_FRAME_U = 20;
    private static final int SLOT_FRAME_V = 3;
    private static final int SLOT_FRAME_W = 16;
    private static final int SLOT_FRAME_H = 16;
    private static final int SLOT_FRAME_OFFSET_X = -8;
    private static final int SLOT_FRAME_OFFSET_Y = -46;

    private static final int VIS_COST_U = 68;
    private static final int VIS_COST_V = 76;
    private static final int VIS_COST_W = 12;
    private static final int VIS_COST_H = 12;
    private static final int VIS_COST_OFFSET_X = -6;
    private static final int VIS_COST_OFFSET_Y = 40;

    private static final float PANEL_SCALE = 2.0F;

    private static final int OUTPUT_OFFSET_X = -8;
    private static final int OUTPUT_OFFSET_Y = -84;

    private static final int GRID_ANCHOR_X = -40;
    private static final int GRID_ANCHOR_Y = -40;
    private static final int GRID_STRIDE = 32;
    private static final int GRID_DIM_MAX = 3;

    private static final int CRYSTAL_BASE_OFFSET_X = 4;
    private static final int CRYSTAL_STRIDE = 20;
    private static final int CRYSTAL_HALF_STRIDE = 10;
    private static final int CRYSTAL_OFFSET_Y = 59;

    private static final int LABEL_OFFSET_Y = -104;
    private static final int VIS_TEXT_OFFSET_Y = 90;

    private static final int VIS_POPUP_OFFSET_X = -15;
    private static final int VIS_POPUP_OFFSET_Y = 75;
    private static final int VIS_POPUP_W = 30;
    private static final int VIS_POPUP_H = 30;

    private static final int LABEL_COLOR = 0xFF504E50;

    private static final int VIS_OVERLAY_TINT = 0x66FFFFFF;

    private static final int ITEM_HIT_SIZE = 16;

    private static final long CYCLE_SECONDS = 1000L;

    private RecipeDisplayWidget() {}

    public static int width() {
        return PANEL_SIZE;
    }

    public static int height() {
        return PANEL_SIZE;
    }

    public static void renderCrafting(
            GuiGraphics graphics,
            int x,
            int y,
            RecipeHolder<?> holder,
            long gameTime
    ) {
        int cx = x + CENTER_OFFSET;
        int cy = y + CENTER_OFFSET;
        Layout layout = collect(holder, registries());
        Font font = Minecraft.getInstance().font;
        if (layout.kind == Kind.ARCANE_SHAPED || layout.kind == Kind.ARCANE_SHAPELESS) {
            drawArcanePanel(graphics, cx, cy);
            drawVisOverlay(graphics, cx, cy);
            drawVisCostText(graphics, font, cx, cy, layout.visCost);
            drawCrystals(graphics, cx, cy, layout.crystals);
        } else {
            drawWorkbenchPanel(graphics, cx, cy);
        }
        drawSlotFrame(graphics, cx, cy);
        drawLabel(graphics, font, cx, cy, layout.kind);
        drawOutput(graphics, cx, cy, layout.output);
        drawInputs(graphics, cx, cy, layout);
    }

    public static @Nullable ItemStack hoverStackForDisplay(
            int x,
            int y,
            RecipeHolder<?> holder,
            long gameTime,
            double mouseX,
            double mouseY
    ) {
        int cx = x + CENTER_OFFSET;
        int cy = y + CENTER_OFFSET;
        Layout layout = collect(holder, registries());
        ItemStack inputHover = hoverInput(cx, cy, layout, mouseX, mouseY);
        if (inputHover != null && !inputHover.isEmpty()) {
            return inputHover;
        }
        if (!layout.output.isEmpty()
                && mouseX >= cx + OUTPUT_OFFSET_X
                && mouseX < cx + OUTPUT_OFFSET_X + ITEM_HIT_SIZE
                && mouseY >= cy + OUTPUT_OFFSET_Y
                && mouseY < cy + OUTPUT_OFFSET_Y + ITEM_HIT_SIZE) {
            return layout.output;
        }
        if (layout.kind == Kind.ARCANE_SHAPED || layout.kind == Kind.ARCANE_SHAPELESS) {
            ItemStack crystalHover = hoverCrystal(cx, cy, layout.crystals, mouseX, mouseY);
            if (crystalHover != null && !crystalHover.isEmpty()) {
                return crystalHover;
            }
        }
        return null;
    }

    public static @Nullable Component hoverPopupForDisplay(
            int x,
            int y,
            RecipeHolder<?> holder,
            double mouseX,
            double mouseY
    ) {
        int cx = x + CENTER_OFFSET;
        int cy = y + CENTER_OFFSET;
        Layout layout = collect(holder, registries());
        if (layout.kind != Kind.ARCANE_SHAPED && layout.kind != Kind.ARCANE_SHAPELESS) {
            return null;
        }
        Font font = Minecraft.getInstance().font;
        int costWidth = font.width(Integer.toString(layout.visCost));
        int popupX = cx - costWidth / 2 + VIS_POPUP_OFFSET_X;
        int popupY = cy + VIS_POPUP_OFFSET_Y;
        if (mouseX >= popupX && mouseX < popupX + VIS_POPUP_W && mouseY >= popupY && mouseY < popupY + VIS_POPUP_H) {
            return Component.translatable("wandtable.text1");
        }
        return null;
    }

    private static HolderLookup.Provider registries() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level == null ? null : mc.level.registryAccess();
    }

    private static void drawWorkbenchPanel(GuiGraphics graphics, int cx, int cy) {
        graphics.pose().pushPose();
        graphics.pose().translate(cx, cy, 0);
        graphics.pose().scale(PANEL_SCALE, PANEL_SCALE, 1F);
        graphics.blit(
                TCScreenTextures.RESEARCH_BOOK_OVERLAY,
                WORKBENCH_PANEL_OFFSET_X, WORKBENCH_PANEL_OFFSET_Y,
                (float) WORKBENCH_PANEL_U, (float) WORKBENCH_PANEL_V,
                WORKBENCH_PANEL_W, WORKBENCH_PANEL_H,
                TCScreenTextures.TEX_SIZE, TCScreenTextures.TEX_SIZE
        );
        graphics.pose().popPose();
    }

    private static void drawArcanePanel(GuiGraphics graphics, int cx, int cy) {
        graphics.pose().pushPose();
        graphics.pose().translate(cx, cy, 0);
        graphics.pose().scale(PANEL_SCALE, PANEL_SCALE, 1F);
        graphics.blit(
                TCScreenTextures.RESEARCH_BOOK_OVERLAY,
                ARCANE_PANEL_OFFSET_X, ARCANE_PANEL_OFFSET_Y,
                (float) ARCANE_PANEL_U, (float) ARCANE_PANEL_V,
                ARCANE_PANEL_W, ARCANE_PANEL_H,
                TCScreenTextures.TEX_SIZE, TCScreenTextures.TEX_SIZE
        );
        graphics.pose().popPose();
    }

    private static void drawSlotFrame(GuiGraphics graphics, int cx, int cy) {
        graphics.pose().pushPose();
        graphics.pose().translate(cx, cy, 0);
        graphics.pose().scale(PANEL_SCALE, PANEL_SCALE, 1F);
        graphics.blit(
                TCScreenTextures.RESEARCH_BOOK_OVERLAY,
                SLOT_FRAME_OFFSET_X, SLOT_FRAME_OFFSET_Y,
                (float) SLOT_FRAME_U, (float) SLOT_FRAME_V,
                SLOT_FRAME_W, SLOT_FRAME_H,
                TCScreenTextures.TEX_SIZE, TCScreenTextures.TEX_SIZE
        );
        graphics.pose().popPose();
    }

    private static void drawVisOverlay(GuiGraphics graphics, int cx, int cy) {
        graphics.pose().pushPose();
        graphics.pose().translate(cx, cy, 0);
        graphics.pose().scale(PANEL_SCALE, PANEL_SCALE, 1F);
        GuiBlend.blitTinted(
                graphics,
                TCScreenTextures.RESEARCH_BOOK_OVERLAY,
                VIS_COST_OFFSET_X, VIS_COST_OFFSET_Y,
                (float) VIS_COST_U, (float) VIS_COST_V,
                VIS_COST_W, VIS_COST_H,
                TCScreenTextures.TEX_SIZE, TCScreenTextures.TEX_SIZE,
                VIS_OVERLAY_TINT
        );
        graphics.pose().popPose();
    }

    private static void drawVisCostText(GuiGraphics graphics, Font font, int cx, int cy, int visCost) {
        String text = Integer.toString(visCost);
        int offset = font.width(text);
        graphics.drawString(font, Component.literal(text), cx - offset / 2, cy + VIS_TEXT_OFFSET_Y, LABEL_COLOR, false);
    }

    private static void drawLabel(GuiGraphics graphics, Font font, int cx, int cy, Kind kind) {
        String key = labelKey(kind);
        if (key == null) return;
        Component text = Component.translatable(key);
        int offset = font.width(text);
        graphics.drawString(font, text, cx - offset / 2, cy + LABEL_OFFSET_Y, LABEL_COLOR, false);
    }

    private static @Nullable String labelKey(Kind kind) {
        return switch (kind) {
            case WORKBENCH_SHAPED -> "recipe.type.workbench";
            case WORKBENCH_SHAPELESS -> "recipe.type.workbenchshapeless";
            case ARCANE_SHAPED -> "recipe.type.arcane";
            case ARCANE_SHAPELESS -> "recipe.type.arcane.shapeless";
            case UNKNOWN -> null;
        };
    }

    private static void drawOutput(GuiGraphics graphics, int cx, int cy, ItemStack output) {
        if (output.isEmpty()) return;
        graphics.renderItem(output, cx + OUTPUT_OFFSET_X, cy + OUTPUT_OFFSET_Y);
    }

    private static void drawInputs(GuiGraphics graphics, int cx, int cy, Layout layout) {
        for (Slot slot : layout.slots) {
            ItemStack stack = pickRotating(slot.cycle, slot.counter);
            if (!stack.isEmpty()) {
                graphics.renderItem(stack, cx + GRID_ANCHOR_X + slot.col * GRID_STRIDE, cy + GRID_ANCHOR_Y + slot.row * GRID_STRIDE);
            }
        }
    }

    private static void drawCrystals(GuiGraphics graphics, int cx, int cy, List<ItemStack> crystals) {
        if (crystals.isEmpty()) return;
        int sz = crystals.size();
        for (int a = 0; a < sz; a++) {
            ItemStack stack = crystals.get(a);
            if (stack.isEmpty()) continue;
            graphics.renderItem(stack, cx + CRYSTAL_BASE_OFFSET_X - sz * CRYSTAL_HALF_STRIDE + a * CRYSTAL_STRIDE, cy + CRYSTAL_OFFSET_Y);
        }
    }

    private static @Nullable ItemStack hoverInput(int cx, int cy, Layout layout, double mouseX, double mouseY) {
        for (Slot slot : layout.slots) {
            int slotX = cx + GRID_ANCHOR_X + slot.col * GRID_STRIDE;
            int slotY = cy + GRID_ANCHOR_Y + slot.row * GRID_STRIDE;
            if (mouseX < slotX || mouseX >= slotX + ITEM_HIT_SIZE) continue;
            if (mouseY < slotY || mouseY >= slotY + ITEM_HIT_SIZE) continue;
            ItemStack stack = pickRotating(slot.cycle, slot.counter);
            if (!stack.isEmpty()) return stack;
        }
        return null;
    }

    private static @Nullable ItemStack hoverCrystal(int cx, int cy, List<ItemStack> crystals, double mouseX, double mouseY) {
        if (crystals.isEmpty()) return null;
        int sz = crystals.size();
        for (int a = 0; a < sz; a++) {
            ItemStack stack = crystals.get(a);
            if (stack.isEmpty()) continue;
            int slotX = cx + CRYSTAL_BASE_OFFSET_X - sz * CRYSTAL_HALF_STRIDE + a * CRYSTAL_STRIDE;
            int slotY = cy + CRYSTAL_OFFSET_Y;
            if (mouseX < slotX || mouseX >= slotX + ITEM_HIT_SIZE) continue;
            if (mouseY < slotY || mouseY >= slotY + ITEM_HIT_SIZE) continue;
            return stack;
        }
        return null;
    }

    private static Layout collect(RecipeHolder<?> holder, HolderLookup.Provider reg) {
        Recipe<?> recipe = holder.value();
        if (recipe instanceof ArcaneShapedCraftingRecipe arcane) {
            return collectArcaneShaped(arcane, reg);
        }
        if (recipe instanceof ArcaneShapelessCraftingRecipe arcane) {
            return collectArcaneShapeless(arcane, reg);
        }
        if (recipe instanceof ShapedRecipe shaped) {
            return collectShaped(shaped, reg);
        }
        if (recipe instanceof ShapelessRecipe shapeless) {
            return collectShapeless(shapeless, reg);
        }
        return new Layout(Kind.UNKNOWN, new ArrayList<>(), resultOf(recipe, reg), 0, List.of());
    }

    private static ItemStack resultOf(Recipe<?> recipe, HolderLookup.Provider reg) {
        return reg == null ? ItemStack.EMPTY : recipe.getResultItem(reg);
    }

    private static List<ItemStack> cycle(Ingredient ingredient) {
        if (ingredient == null || ingredient.hasNoItems()) {
            return List.of();
        }
        return List.of(ingredient.getItems());
    }

    private static List<ItemStack> crystals(IArcaneRecipe arcane) {
        List<ItemStack> list = new ArrayList<>();
        for (AspectInstance entry : arcane.getCrystals().entries()) {
            ItemStack stack = EssentiaCrystalFactory.of(entry.aspect(), entry.amount());
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }
        return list;
    }

    private static Layout collectShaped(ShapedRecipe shaped, HolderLookup.Provider reg) {
        int rw = shaped.getWidth();
        int rh = shaped.getHeight();
        NonNullList<Ingredient> ingredients = shaped.getIngredients();
        List<Slot> slots = gridSlots(rw, rh, ingredients);
        return new Layout(Kind.WORKBENCH_SHAPED, slots, resultOf(shaped, reg), 0, List.of());
    }

    private static Layout collectShapeless(ShapelessRecipe shapeless, HolderLookup.Provider reg) {
        List<Slot> slots = linearSlots(shapeless.getIngredients());
        return new Layout(Kind.WORKBENCH_SHAPELESS, slots, resultOf(shapeless, reg), 0, List.of());
    }

    private static Layout collectArcaneShaped(ArcaneShapedCraftingRecipe arcane, HolderLookup.Provider reg) {
        int rw = arcane.getWidth();
        int rh = arcane.getHeight();
        List<Slot> slots = gridSlots(rw, rh, arcane.getIngredients());
        return new Layout(Kind.ARCANE_SHAPED, slots, resultOf(arcane, reg), arcane.getBaseVis(), crystals(arcane));
    }

    private static Layout collectArcaneShapeless(ArcaneShapelessCraftingRecipe arcane, HolderLookup.Provider reg) {
        List<Slot> slots = linearSlots(arcane.ingredients());
        return new Layout(Kind.ARCANE_SHAPELESS, slots, resultOf(arcane, reg), arcane.getBaseVis(), crystals(arcane));
    }

    private static List<Slot> gridSlots(int rw, int rh, List<Ingredient> ingredients) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < rw && i < GRID_DIM_MAX; i++) {
            for (int j = 0; j < rh && j < GRID_DIM_MAX; j++) {
                int index = i + j * rw;
                if (index >= ingredients.size()) continue;
                List<ItemStack> c = cycle(ingredients.get(index));
                if (c.isEmpty()) continue;
                slots.add(new Slot(i, j, index, c));
            }
        }
        return slots;
    }

    private static List<Slot> linearSlots(List<Ingredient> ingredients) {
        List<Slot> slots = new ArrayList<>();
        int cap = Math.min(ingredients.size(), 9);
        for (int i = 0; i < cap; i++) {
            List<ItemStack> c = cycle(ingredients.get(i));
            if (c.isEmpty()) continue;
            slots.add(new Slot(i % GRID_DIM_MAX, i / GRID_DIM_MAX, i, c));
        }
        return slots;
    }

    private static ItemStack pickRotating(List<ItemStack> stacks, int counter) {
        if (stacks.isEmpty()) return ItemStack.EMPTY;
        long wall = System.currentTimeMillis() / CYCLE_SECONDS;
        int index = (int) Math.floorMod((long) counter + wall, (long) stacks.size());
        return stacks.get(index);
    }

    private enum Kind {
        WORKBENCH_SHAPED,
        WORKBENCH_SHAPELESS,
        ARCANE_SHAPED,
        ARCANE_SHAPELESS,
        UNKNOWN
    }

    private record Slot(int col, int row, int counter, List<ItemStack> cycle) {}

    private record Layout(Kind kind, List<Slot> slots, ItemStack output, int visCost, List<ItemStack> crystals) {}
}
