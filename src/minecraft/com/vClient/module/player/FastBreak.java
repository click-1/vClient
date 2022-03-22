package com.vClient.module.player;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak", Keyboard.CHAR_NONE, Category.PLAYER, "Increase player mining speed.");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Break Damage", this, 0.5F, 0.01F, 1.0F, false));
    }
}
