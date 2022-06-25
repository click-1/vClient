package com.vClient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class MathUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        float dist = f > 180F ? 360F - f : f;
        return dist;
    }

    public static boolean isInFOV(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = MathUtil.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtil.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }
}
