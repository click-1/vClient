package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Speed extends Module {
    String mode;
    public Speed() {
        super("Speed", Keyboard.CHAR_NONE, Category.MOVEMENT, "Modify player movement.");
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("NCPHop");
        options.add("SlowHop");
        options.add("Watchdog");
        this.setDisplayMode("Watchdog");
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
        vClient.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "Watchdog", options));
    }
    @Override
    public void onEnable() {
        mode = vClient.instance.settingsManager.getSettingByName("Speed Mode").getValString();
        if (mode.equals("NCPHop"))
            mc.timer.timerSpeed = 1.0865F;
        this.setDisplayMode(mode);
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
        mode = mode.toLowerCase();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (MovementUtil.isMoving()) {
            if (mc.thePlayer.onGround)
                mc.thePlayer.jump();
            switch (mode) {
                case "ncphop":  MovementUtil.strafe();
                                break;
                case "slowhop": MovementUtil.strafe(MovementUtil.getSpeed() * .995f);
                                break;
                case "watchdog": MovementUtil.strafe(MovementUtil.getSpeed() * 1.0055f);
                                break;
            }
        } else {
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }
}
