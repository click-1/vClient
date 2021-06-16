package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Fly extends Module {
    private String mode = vClient.instance.settingsManager.getSettingByName("Fly Mode").getValString();
    public Fly() {
        super("Fly", Keyboard.CHAR_NONE, Category.MOVEMENT);
    }
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Vanilla");
        vClient.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Hypixel", options));
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setDisplayName("Fly + " + mode);
        if (mode.equalsIgnoreCase("Hypixel")) {
            double y, y1;
            mc.thePlayer.motionY = 0;
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                y = mc.thePlayer.posY - 1.0E-10D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
            }
            y1 = mc.thePlayer.posY + 1.0E-10D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);
        }
        if (mode.equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = true;
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        if (mode.equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.capabilities.isFlying = false;
        }
    }
}
