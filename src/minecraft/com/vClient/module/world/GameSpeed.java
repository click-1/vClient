package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MathUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class GameSpeed extends Module {
    public GameSpeed() {
        super("GameSpeed", Keyboard.CHAR_NONE, Category.WORLD, "Change player game speed.");
        this.setDisplayNotif(true);
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("GameSpeed", this, 1.3, 1, 2, false));
        updateDisplay(String.valueOf(1.3));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        float gs = (float) MathUtil.round(vClient.instance.settingsManager.getSettingByName("GameSpeed").getValDouble(), 2);
        mc.timer.timerSpeed = gs;
        updateDisplay(String.valueOf(gs));
    }

    @Override
    public void onEnable() {
        float gs = (float) MathUtil.round(vClient.instance.settingsManager.getSettingByName("GameSpeed").getValDouble(), 2);
        mc.timer.timerSpeed = gs;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }
}
