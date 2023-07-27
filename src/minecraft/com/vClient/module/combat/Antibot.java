package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.event.events.EventWorld;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ColorUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.util.Map;

public class AntiBot extends Module {
    private final Map<Integer, Integer> invalidGround = new HashMap<>();

    public AntiBot() {
        super("AntiBot", Keyboard.CHAR_NONE, Category.COMBAT, "Prevent KillAura from attacking server-side bots.");
    }

    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (mc.thePlayer == null || mc.theWorld == null)
            return;

        final Packet<?> packet = event.getPacket();
        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity packetEntity = (S14PacketEntity) event.getPacket();
            final Entity entity = packetEntity.getEntity(mc.theWorld);
            if (entity instanceof EntityPlayer) {
                if (packetEntity.getOnGround()) {
                    if (entity.prevPosY != entity.posY)
                        invalidGround.put(entity.getEntityId(), invalidGround.getOrDefault(entity.getEntityId(), 0) + 1);
                } else {
                    final int currentVL = invalidGround.getOrDefault(entity.getEntityId(), 0) / 2;
                    if (currentVL <= 0)
                        invalidGround.remove(entity.getEntityId());
                    else
                        invalidGround.put(entity.getEntityId(), currentVL);
                }
            }
        }
    }

    public boolean isBot(final Entity entity) {
        if (invalidGround.getOrDefault(entity.getEntityId(), 0) >= 10)
            return true;
        if (entity.getEntityId() >= 1000000000 || entity.getEntityId() <= -1)
            return true;
        if (entity.rotationPitch > 90F || entity.rotationPitch < -90F)
            return true;

        final String targetName = ColorUtil.stripColor(entity.getDisplayName().getFormattedText());
        if (targetName != null) {
            for (final NetworkPlayerInfo entry : mc.getNetHandler().getPlayerInfoMap()) {
                final String networkName = ColorUtil.stripColor(entry.getGameProfile().getName());
                if (networkName == null)
                    continue;
                if (targetName.contains(networkName))
                    return false;
            }
            return true;
        }
        return entity.getName().isEmpty() || entity.getName().equals(mc.thePlayer.getName());
    }

    @EventTarget
    public void onWorld(EventWorld event) {
        invalidGround.clear();
    }

    @Override
    public void onDisable() {
        invalidGround.clear();
        super.onDisable();
    }
}
