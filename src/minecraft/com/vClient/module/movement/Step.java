package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventStep;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    private double X, Y, Z;
    public Step() {
        super("Step", Keyboard.CHAR_NONE, Category.MOVEMENT, "Step over blocks upon collision.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!mc.gameSettings.keyBindJump.isKeyDown())
            mc.thePlayer.stepHeight = 1f;
        else
            mc.thePlayer.stepHeight = 0.6f;
    }
    @EventTarget
    public void onStep(EventStep event) {
        X = mc.thePlayer.posX;
        Y = mc.thePlayer.posY;
        Z = mc.thePlayer.posZ;
        if (mc.thePlayer.getEntityBoundingBox().minY - Y > 0.6) {
            mc.thePlayer.isAirBorne = true;
            mc.thePlayer.triggerAchievement(StatList.jumpStat);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + 0.41999998688698, Z, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + 0.7531999805212, Z, false));
        }
    }
    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
        super.onDisable();
    }
}
