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
    public Speed() {
        super("Speed", Keyboard.CHAR_NONE, Category.MOVEMENT, "Modify player movement.");
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("NCPHop");
        options.add("Watchdog");
        vClient.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "NCPHop", options));
    }
    @Override
    public void onEnable() {
        String mode = vClient.instance.settingsManager.getSettingByName("Speed Mode").getValString();
        if (mode.equalsIgnoreCase("NCPHop"))
            mc.timer.timerSpeed = 1.0865F;
        if (mode.equalsIgnoreCase("Watchdog"))
            mc.timer.timerSpeed = 1.0F;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = vClient.instance.settingsManager.getSettingByName("Speed Mode").getValString();
        if (MovementUtil.isMoving()) {
            //vClient.addChatMessage(String.valueOf(MovementUtil.getSpeed()*20.0));
            if (mc.thePlayer.onGround)
                mc.thePlayer.jump();
            if (mode.equalsIgnoreCase("NCPHop"))
                MovementUtil.strafe();
            else
                MovementUtil.strafe(MovementUtil.getSpeed() * .995f);

        } else {
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }
}
