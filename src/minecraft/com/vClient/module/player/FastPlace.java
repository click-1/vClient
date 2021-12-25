package com.vClient.module.player;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", Keyboard.CHAR_NONE, Category.PLAYER, "Remove block placing delay.");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Block Delay", this, 2, 0, 4, true));
    }
}
