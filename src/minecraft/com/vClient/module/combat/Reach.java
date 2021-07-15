package com.vClient.module.combat;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Reach() {
        super("Reach", Keyboard.CHAR_NONE, Category.COMBAT, "Increase player attack range.");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Reach", this, 3.0, 3.0, 6.0, false));
    }
}
