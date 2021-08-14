package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AntiBot extends Module {
    public AntiBot() {
        super("AntiBot", Keyboard.CHAR_NONE, Category.COMBAT, "Prevent KillAura from attacking server-side bots. ");
    }
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Advanced");
        options.add("Watchdog");
        vClient.instance.settingsManager.rSetting(new Setting("AntiBot Mode", this, "Advanced", options));
    }
    @EventTarget
    public void onEventReceivePacket(EventReceivePacket event) {
        String mode = vClient.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
        if (mode.equalsIgnoreCase("Advanced") && event.getPacket() instanceof S0CPacketSpawnPlayer) {
            this.setDisplayName("AntiBot \u00A77" + mode);
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) event.getPacket();
            double posX = packet.getX() / 320D;
            double posY = packet.getY() / 320D;
            double posZ = packet.getZ() / 320D;
            double diffX = mc.thePlayer.posX - posX;
            double diffY = mc.thePlayer.posY - posY;
            double diffZ = mc.thePlayer.posZ - posZ;
            double dist = Math.sqrt(diffX*diffX+diffY*diffY+diffZ*diffZ);
            if (dist <= 17D && posX != mc.thePlayer.posX && posY != mc.thePlayer.posY && posZ != mc.thePlayer.posZ) {
                event.setCancelled(true);
            }
        }
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = vClient.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
        if (mode.equalsIgnoreCase("Watchdog")) {
            this.setDisplayName("AntiBot \u00A77" + mode);
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity.isInvisible() && entity != mc.thePlayer)
                    mc.theWorld.removeEntity(entity);
            }
        }
    }
}
