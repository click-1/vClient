package com.vClient.module.misc;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class GameSpeed extends Module {
    private float gs;
    public GameSpeed() {
        super("GameSpeed", Keyboard.CHAR_NONE, Category.MISC, "Change player game speed.");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("GameSpeed", this, 2, 1, 2, false));
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.timer.timerSpeed = gs;
    }
    @Override
    public void onEnable() {
        gs = (float) vClient.instance.settingsManager.getSettingByName("GameSpeed").getValDouble();
        mc.timer.timerSpeed = gs;
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }
}
