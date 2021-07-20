package com.vClient.module.render;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", Keyboard.CHAR_NONE, Category.RENDER, "Increase gamma.");
    }
    @Override
    public void onEnable() {
        super.onEnable();
        mc.gameSettings.gammaSetting = 100;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 1;
    }
}
