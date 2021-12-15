package com.vClient.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil {
    public static boolean isAnimal(final Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem ||
                entity instanceof EntityBat;
    }
    public static boolean isMob(final Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntitySlime ||
                entity instanceof EntityGhast || entity instanceof EntityDragon;
    }
    public static boolean isPlayer(final Entity entity) {
        return entity instanceof EntityPlayer && !((EntityPlayer) entity).isSpectator();
    }
}
