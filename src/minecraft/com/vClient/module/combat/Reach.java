package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MathUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Reach() {
        super("Reach", Keyboard.CHAR_NONE, Category.COMBAT, "Increase player attack range.");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Reach", this, 4.0, 3.0, 6.0, false));
        this.setDisplayMode(String.valueOf(4.0));
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setDisplayMode(String.valueOf(MathUtil.round(vClient.instance.settingsManager.getSettingByName("Reach").getValDouble(), 2)));
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
    }
}
