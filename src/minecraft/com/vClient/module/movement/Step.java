package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventStep;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    private double X, Y, Z;
    public Step() {
        super("Step", Keyboard.CHAR_NONE, Category.MOVEMENT, "Step over blocks upon collision.");
    }
    @Override
    public void onEnable() {
        mc.thePlayer.stepHeight = 1f;
        super.onEnable();
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
    private boolean validStep() {
        double radians = MovementUtil.getDirection();
        radians = ((radians % (2*Math.PI)) + 2*Math.PI) % (2*Math.PI);
        BlockPos topPos, bottomPos;
        double topHeight, bottomHeight;

        if (radians >= Math.PI/4 && radians < 3*Math.PI/4) {
            bottomPos = mc.thePlayer.getPosition().west();

        }

        else if (radians >= 3*Math.PI/4 && radians < 5*Math.PI/4) {
            bottomPos = mc.thePlayer.getPosition().north();

        }

        else if (radians >= 5*Math.PI/4 && radians < 7*Math.PI/4) {
            bottomPos = mc.thePlayer.getPosition().east();

        }

        else {
            bottomPos = mc.thePlayer.getPosition().south();

        }

        topPos = bottomPos.north();
        Block topBlock = mc.theWorld.getBlockState(topPos).getBlock();
        Block bottomBlock = mc.theWorld.getBlockState(bottomPos).getBlock();

        AxisAlignedBB topBox = topBlock.getCollisionBoundingBox(mc.thePlayer.worldObj, topPos, mc.thePlayer.worldObj.getBlockState(topPos));
        AxisAlignedBB bottomBox = bottomBlock.getCollisionBoundingBox(mc.thePlayer.worldObj, bottomPos, mc.thePlayer.worldObj.getBlockState(bottomPos));
        AxisAlignedBB playerBox = mc.thePlayer.getEntityBoundingBox();
        if (topBox == null || topBlock.isPassable(mc.thePlayer.worldObj, topPos)) {
            return bottomBox.maxY - playerBox.minY <= 1.0;
        }
        else {
            return topBox.maxY - playerBox.minY <= 1.0;
        }
    }
    private void customJump() {
        mc.thePlayer.motionY = .37;

        if (mc.thePlayer.isPotionActive(Potion.jump))
        {
            mc.thePlayer.motionY += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;

        mc.thePlayer.isAirBorne = true;
    }
}
