package com.leclowndu93150.thaumcraft.client.hud;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;

public final class LeftHudStack implements LayeredDraw.Layer {
    public interface Gauge {
        boolean visible(Minecraft mc, LocalPlayer player);

        int height();

        @Nullable
        String exclusiveGroup();

        void render(GuiGraphics graphics, DeltaTracker deltaTracker);
    }

    private final List<Gauge> gauges;

    public LeftHudStack(List<Gauge> gauges) {
        this.gauges = List.copyOf(gauges);
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }
        List<String> claimedGroups = new ArrayList<>();
        int y = 0;
        for (Gauge gauge : gauges) {
            if (!gauge.visible(mc, player)) {
                continue;
            }
            String group = gauge.exclusiveGroup();
            if (group != null) {
                if (claimedGroups.contains(group)) {
                    continue;
                }
                claimedGroups.add(group);
            }
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, y, 0.0F);
            gauge.render(graphics, deltaTracker);
            graphics.pose().popPose();
            y += gauge.height();
        }
    }
}
