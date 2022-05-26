package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Speed extends Module {
    public Speed() {
        super("Speed", Keyboard.CHAR_NONE, Category.MOVEMENT, "Modify player movement.");
        this.setDisplayNotif(true);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("HypixelHop");
        options.add("NCPHop");
        options.add("Watchdog");
        updateDisplay("Watchdog");
        vClient.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "Watchdog", options));
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = vClient.instance.settingsManager.getSettingByName("Speed Mode").getValString();
        updateDisplay(mode);
        if (MovementUtil.isMoving()) {
            switch (mode) {
                case "NCPHop":
                    mc.timer.timerSpeed = 1.0865F;
                    if (mc.thePlayer.onGround)
                        mc.thePlayer.jump();
                    MovementUtil.strafe();
                    break;
                case "HypixelHop":
                    mc.timer.timerSpeed = 1F;
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        float speed = MovementUtil.getSpeed() < 0.56F ? MovementUtil.getSpeed() * 1.045F : 0.56F;
                        if (mc.thePlayer.onGround && mc.thePlayer.isPotionActive(Potion.moveSpeed))
                            speed *= 1F + 0.13F * (1 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier());
                        MovementUtil.strafe(speed);
                        return;
                    } else if (mc.thePlayer.motionY < 0.2D)
                        mc.thePlayer.motionY -= 0.02D;
                    else {
                        MovementUtil.strafe(MovementUtil.getSpeed() * 1.01889F);
                    }
                case "Watchdog":
                    mc.timer.timerSpeed = 1F;
                    if (mc.thePlayer.onGround)
                        mc.thePlayer.jump();
                    MovementUtil.strafe(MovementUtil.getSpeed() * 1.0055f);
                    break;
            }
        } else {
            mc.thePlayer.motionX = 0D;
            mc.thePlayer.motionZ = 0D;
        }
    }

//    @EventTarget
//    public void onPacket(EventReceivePacket event) {
//        if (event.getPacket() instanceof S18PacketEntityTeleport) {
//            vClient.addChatMessage("Disabled " + "\2473" + "Speed" + "\2477" + " due to lagback.");
//            this.toggle();
//        }
//    }
}
