package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;

public class FreeCam extends Module {
    private EntityOtherPlayerMP fakePlayer;
    private double oldX;
    private double oldY;
    private double oldZ;
    private float oldYaw;
    private float oldPitch;

    public FreeCam() {
        super("FreeCam", Keyboard.CHAR_NONE, Category.WORLD, "Allow camera to move out of body.");
    }

    @Override
    public void onEnable() {
        oldX = mc.thePlayer.posX;
        oldY = mc.thePlayer.posY;
        oldZ = mc.thePlayer.posZ;
        oldYaw = mc.thePlayer.rotationYaw;
        oldPitch = mc.thePlayer.rotationPitch;

        fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        fakePlayer.clonePlayer(mc.thePlayer, true);

        fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
        fakePlayer.rotationPitchHead = mc.thePlayer.rotationPitchHead;
        fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);

        mc.theWorld.addEntityToWorld(-1000, fakePlayer);
        mc.thePlayer.noClip = true;

        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null || fakePlayer == null)
            return;

        mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, oldYaw, oldPitch);
        mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
        fakePlayer = null;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;

        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.thePlayer.noClip = true;
        mc.thePlayer.fallDistance = 0;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown())
            mc.thePlayer.motionY = 1f;
        if (mc.gameSettings.keyBindSneak.isKeyDown())
            mc.thePlayer.motionY = -1f;
        MovementUtil.strafe(1f);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction)
            event.setCancelled(true);
    }
}
