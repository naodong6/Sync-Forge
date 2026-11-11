package net.sumik.sync.client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class HudController {
    private static final Options GAME_OPTIONS = Minecraft.getInstance().options;
    private static Boolean wasHudHidden;

    public static void show() {
        setHudHidden(false);
    }

    public static void hide() {
        setHudHidden(true);
    }

    private static void setHudHidden(boolean value) {
        if (wasHudHidden == null) {
            wasHudHidden = GAME_OPTIONS.hideGui;
        }
        GAME_OPTIONS.hideGui = value;
    }

    public static void restore() {
        if (wasHudHidden != null) {
            GAME_OPTIONS.hideGui = wasHudHidden;
            wasHudHidden = null;
        }
    }
}