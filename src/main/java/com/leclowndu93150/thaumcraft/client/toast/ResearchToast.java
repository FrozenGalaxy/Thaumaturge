package com.leclowndu93150.thaumcraft.client.toast;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ResearchToast implements Toast {
    private static final long DURATION_MS = 5000L;
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");
    private static final int TITLE_COLOR = 0xFF552200;
    private static final int SUBTITLE_COLOR = 0xFF000000;

    private final Component title;
    private final Component subtitle;
    private final ItemStack icon;
    private final ResourceLocation id;

    public ResearchToast(ResourceLocation researchId, Component title, Component subtitle, ItemStack icon) {
        this.id = researchId;
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon == null || icon.isEmpty() ? new ItemStack(Items.PAPER) : icon;
    }

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent manager, long timeVisible) {
        graphics.blitSprite(BACKGROUND_SPRITE, 0, 0, width(), height());
        graphics.drawString(manager.getMinecraft().font, title, 30, 7, TITLE_COLOR, false);
        graphics.drawString(manager.getMinecraft().font, subtitle, 30, 18, SUBTITLE_COLOR, false);
        graphics.renderFakeItem(icon, 8, 8);
        return timeVisible >= DURATION_MS * manager.getNotificationDisplayTimeMultiplier()
                ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    @Override
    public Object getToken() {
        return id;
    }
}
