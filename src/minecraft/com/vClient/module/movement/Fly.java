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
        this.setDisplayNotif(true);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Normal");
        options.add("Hypixel");
        options.add("Vanilla");
        this.setDisplayMode("Normal");
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
        vClient.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Normal", options));
        vClient.instance.settingsManager.rSetting(new Setting("Flight Speed", this, 0.77, 0, 2, false));
        vClient.instance.settingsManager.rSetting(new Setting("Vert Speed", this, 0.0, 0, 2, false));
    }

    @Override
    public void onEnable() {
        mode = vClient.instance.settingsManager.getSettingByName("Fly Mode").getValString();
        normalSpeed = (float) vClient.instance.settingsManager.getSettingByName("Flight Speed").getValDouble();
        verticalSpeed = (float) vClient.instance.settingsManager.getSettingByName("Vert Speed").getValDouble();
        this.setDisplayMode(mode);
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());
        mode = mode.toLowerCase();

        //Damage fly Moon script
        for (int i = 0; i <= 3 / 0.0625; i++) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        }
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));

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
