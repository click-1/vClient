package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Fly extends Module {
    private String mode;
    private float normalSpeed, verticalSpeed;

    public Fly() {
        super("Fly", Keyboard.CHAR_NONE, Category.MOVEMENT, "Enable player to fly.");
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Normal");
        options.add("Hypixel");
        options.add("Vanilla");
        vClient.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Normal", options));
        vClient.instance.settingsManager.rSetting(new Setting("Flight Speed", this, 1.5, 0, 2, false));
        vClient.instance.settingsManager.rSetting(new Setting("Vert Speed", this, 0.4, 0, 2, false));
    }

    @Override
    public void onEnable() {
        mode = vClient.instance.settingsManager.getSettingByName("Fly Mode").getValString().toLowerCase();
        normalSpeed = (float) vClient.instance.settingsManager.getSettingByName("Flight Speed").getValDouble();
        verticalSpeed = (float) vClient.instance.settingsManager.getSettingByName("Vert Speed").getValDouble();
        this.setDisplayName("Fly + " + mode);
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        switch (mode) {
            case "normal":
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.thePlayer.motionY = verticalSpeed;
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.thePlayer.motionY = -verticalSpeed;
                MovementUtil.strafe(normalSpeed);
                break;
            case "hypixel":
                mc.thePlayer.capabilities.isFlying = false;
                double y, y1;
                mc.thePlayer.motionY = 0;
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    y = mc.thePlayer.posY - 1.0E-10D;
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
                }
                y1 = mc.thePlayer.posY + 1.0E-10D;
                mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);
                break;
            case "vanilla":
                mc.thePlayer.capabilities.isFlying = true;
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }
}
