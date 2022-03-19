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
    private float gs;
    public GameSpeed() {
        super("GameSpeed", Keyboard.CHAR_NONE, Category.WORLD, "Change player game speed.");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("GameSpeed", this, 1.39, 1, 2, false));
        this.setDisplayMode(String.valueOf(1.3));
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.timer.timerSpeed = gs;
    }
    @Override
    public void onEnable() {
        gs = (float) MathUtil.round(vClient.instance.settingsManager.getSettingByName("GameSpeed").getValDouble(), 2);
        mc.timer.timerSpeed = gs;
        this.setDisplayMode(String.valueOf(MathUtil.round(gs, 2)));
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }
}
