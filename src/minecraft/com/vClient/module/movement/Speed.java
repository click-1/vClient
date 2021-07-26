package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public Speed() {
        super("Speed", Keyboard.CHAR_NONE, Category.MOVEMENT, "Modify player movement.");
    }
    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.0865F;
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

            MovementUtil.strafe();
        } else {
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }
}
