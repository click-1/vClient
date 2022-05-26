package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventMove;
import com.vClient.event.events.EventStep;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Step extends Module {
    private int smoothState = 0;

    public Step() {
        super("Step", Keyboard.CHAR_NONE, Category.MOVEMENT, "Step over blocks upon collision.");
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Instant");
        options.add("Smooth");
        options.add("Stutter");
        updateDisplay("Instant");
        vClient.instance.settingsManager.rSetting(new Setting("Step Mode", this, "Instant", options));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = vClient.instance.settingsManager.getSettingByName("Step Mode").getValString();
        updateDisplay(mode);
        if (mode.equalsIgnoreCase("Smooth") || mc.thePlayer.posY - mc.thePlayer.lastTickPosY < 0 || mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.stepHeight = 0.6f;
            return;
        }

        if (mode.equalsIgnoreCase("Instant"))
            mc.thePlayer.stepHeight = 1f;
        else
            mc.thePlayer.stepHeight = 0.6f;
        if (mc.thePlayer.isCollidedHorizontally)
            mc.thePlayer.stepHeight = 1f;
    }

    @EventTarget
    public void onStep(EventStep event) {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        if (mc.thePlayer.getEntityBoundingBox().minY - y > 0.6) {
            spoofJump();
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.41999998688698, z, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.7531999805212, z, false));
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (!vClient.instance.settingsManager.getSettingByName("Step Mode").getValString().equalsIgnoreCase("Smooth")
            || !mc.thePlayer.isCollidedHorizontally || mc.gameSettings.keyBindJump.isKeyDown())
            return;

        if (mc.thePlayer.onGround && couldStep()) {
            spoofJump();
            mc.thePlayer.motionY = 0.0;
            event.setY(0.41999998688698);
            smoothState = 1;
        } else if (smoothState == 1) {
            event.setY(0.7531999805212);
            smoothState = 2;
        } else if (smoothState == 2) {
            double yaw = MovementUtil.getDirection();
            event.setY(1.001335979112147);
            //event.setX(-Math.sin(yaw) * 0.7);
            //event.setZ(Math.cos(yaw) * 0.7);
            smoothState = 0;
        }
    }

    private boolean couldStep() {
        double yaw = MovementUtil.getDirection();
        double x = -Math.sin(yaw) * 0.4;
        double z = Math.cos(yaw) * 0.4;
        return mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 1.001335979112147, z)).isEmpty();
    }

    private void spoofJump() {
        mc.thePlayer.isAirBorne = true;
        mc.thePlayer.triggerAchievement(StatList.jumpStat);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
        super.onDisable();
    }
}
