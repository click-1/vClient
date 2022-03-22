package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    private double horizontal, vertical, frequency;

    public Velocity() {
        super("Velocity", Keyboard.CHAR_NONE, Category.PLAYER, "Remove player knockback effects.");
    }

    @Override
    public void onEnable() {
        horizontal = vClient.instance.settingsManager.getSettingByName("Horizontal").getValDouble();
        vertical = vClient.instance.settingsManager.getSettingByName("Vertical").getValDouble();
        frequency = vClient.instance.settingsManager.getSettingByName("Frequency").getValDouble();
        super.onEnable();
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Horizontal", this, 0, 0, 1, false));
        vClient.instance.settingsManager.rSetting(new Setting("Vertical", this, 0, 0, 1, false));
        vClient.instance.settingsManager.rSetting(new Setting("Frequency", this, 1, 0, 1, false));
    }

    @EventTarget
    public void onEventReceivePacket(EventReceivePacket event) {
        Packet packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity) {
            if (mc.theWorld.getEntityByID(((S12PacketEntityVelocity) packet).getEntityID()) != mc.thePlayer || Math.random() > frequency)
                return;
            S12PacketEntityVelocity pack = (S12PacketEntityVelocity) packet;
            if (horizontal == 0 && vertical == 0)
                event.setCancelled(true);
            else {
                pack.motionX = (int) (pack.getMotionX() * horizontal);
                pack.motionZ = (int) (pack.getMotionZ() * horizontal);
                pack.motionY = (int) (pack.getMotionX() * vertical);
            }
        }
        if (event.getPacket() instanceof S27PacketExplosion)
            event.setCancelled(true);
    }
}
