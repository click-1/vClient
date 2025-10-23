package com.vClient.module.visual;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event3D;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.module.minigames.MMDetector;
import com.vClient.module.minigames.Paintball;
import com.vClient.util.ClockUtil;
import com.vClient.util.ColorUtil;
import com.vClient.util.RenderUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.vClient.util.RenderUtil.setGlCap;
import static org.lwjgl.opengl.GL11.*;


public class ESP extends Module {
    private final Color goldColor = new Color(255, 255, 85);
    private final Color diamondColor = new Color(85, 255, 255);
    private final Color potionColor = new Color(31, 31, 161);
    private final ClockUtil clock = new ClockUtil();
    private final List<BlockPos> chestPosList = new ArrayList<>();
    private final List<BlockPos> diamondPosList = new ArrayList<>();
    private Thread thread;

    public ESP() {
        super("ESP", Keyboard.CHAR_NONE, Category.VISUAL, "Extrasensory perception of entities & dropped items.");
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Box");
        options.add("Outline");
        updateDisplay("Box");
        vClient.instance.settingsManager.rSetting(new Setting("Entities", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("MW Drops", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("Chests", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Diamond", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("ESP Mode", this, "Box", options));
        vClient.instance.settingsManager.rSetting(new Setting("Search radius", this, 64, 5, 128, true));
    }

    @EventTarget
    public void on3D(Event3D event) {
        if (vClient.instance.settingsManager.getSettingByName("Entities").getValBoolean()) {
            String mode = vClient.instance.settingsManager.getSettingByName("ESP Mode").getValString();
            updateDisplay(mode);
            this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());

            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (entity != mc.thePlayer && entity instanceof EntityLivingBase && RenderUtil.canDisplay((EntityLivingBase) entity)) {
                    final EntityLivingBase entityLiving = (EntityLivingBase) entity;
                    if (mc.playerController.isSpectator() && RenderUtil.cameraPosDistance(entityLiving) < 6.0)
                        continue;
                    Color color;
                    MMDetector mmDetector = (MMDetector) vClient.instance.moduleManager.getModulebyName("MMDetector");
                    Paintball paintball = (Paintball) vClient.instance.moduleManager.getModulebyName("Paintball");
                    if (mmDetector.isToggled() && mmDetector.murderers.contains(entity.getEntityId()))
                        color = new Color(255, 85, 85);
                    else if (mmDetector.isToggled() && mmDetector.detectives.contains(entity.getEntityId()))
                        color = new Color(85, 255, 255);
                    else if (paintball.isToggled() && paintball.ffs.containsKey(entity.getEntityId()))
                        color = ColorUtil.getRainbowColor(12f,0,1f,0.8f,255);
                    else
                        color = getColor(entityLiving);

                    RenderUtil.drawEntityBox(entity, color, mode.equalsIgnoreCase("Outline"));
                }
            }
        }

        if (vClient.instance.settingsManager.getSettingByName("MW Drops").getValBoolean()) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    EntityItem entityItem = (EntityItem) entity;
                    Item item = entityItem.getEntityItem() != null ? entityItem.getEntityItem().getItem() : null;
                    if (item == Items.gold_ingot || item == Items.golden_apple)
                        RenderUtil.drawEntityBox(entity, goldColor, false);
                    if (item == Items.diamond || item == Items.diamond_sword)
                        RenderUtil.drawEntityBox(entity, diamondColor, false);
                    if (item == Items.potionitem)
                        RenderUtil.drawEntityBox(entity, potionColor, false);
                }
            }
        }

        if (vClient.instance.settingsManager.getSettingByName("Chests").getValBoolean()) {
            synchronized (chestPosList) {
                final Color color = new Color(122, 255, 127);
                for (final BlockPos pos : chestPosList) {
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    setGlCap(GL_BLEND, true);
                    setGlCap(GL_TEXTURE_2D, false);
                    setGlCap(GL_DEPTH_TEST, false);
                    glDepthMask(false);
                    GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 100 / 255F);

                    RenderUtil.drawBlockBox(pos);
                }
            }
        }

        if (vClient.instance.settingsManager.getSettingByName("Diamond").getValBoolean()) {
            synchronized (diamondPosList) {
                for (final BlockPos pos : diamondPosList) {
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    setGlCap(GL_BLEND, true);
                    setGlCap(GL_TEXTURE_2D, false);
                    setGlCap(GL_DEPTH_TEST, false);
                    glDepthMask(false);
                    GlStateManager.color(diamondColor.getRed() / 255F, diamondColor.getGreen() / 255F, diamondColor.getBlue() / 255F, 100 / 255F);

                    RenderUtil.drawBlockBox(pos);
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!vClient.instance.settingsManager.getSettingByName("Chests").getValBoolean() && !vClient.instance.settingsManager.getSettingByName("Diamond").getValBoolean())
            return;

        clock.updateTime();
        if (clock.elapsedTime() > 2000 && (thread == null || !thread.isAlive())) {
            int radius = (int) vClient.instance.settingsManager.getSettingByName("Search radius").getValDouble();
            thread = new Thread(() -> {
                final List<BlockPos> chestList = new ArrayList<>();
                final List<BlockPos> diaList = new ArrayList<>();

                for(int x = -radius; x < radius; x++) {
                    for(int y = radius; y > -radius; y--) {
                        for(int z = -radius; z < radius; z++) {
                            final int xPos = ((int) mc.thePlayer.posX + x);
                            final int yPos = ((int) mc.thePlayer.posY + y);
                            final int zPos = ((int) mc.thePlayer.posZ + z);

                            final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block instanceof BlockChest || block instanceof BlockEnderChest) {
                                chestList.add(blockPos);
                            }
                            if (block == Blocks.diamond_ore) {
                                diaList.add(blockPos);
                            }
                        }
                    }
                }
                clock.resetTime();

                synchronized(chestPosList) {
                    chestPosList.clear();
                    chestPosList.addAll(chestList);
                }
                synchronized(diamondPosList) {
                    diamondPosList.clear();
                    diamondPosList.addAll(diaList);
                }
            });
            thread.start();
        }
    }

    private Color getColor(final EntityLivingBase entity) {
        if (entity.hurtTime > 0)
            return Color.RED;
        final char[] chars = entity.getDisplayName().getFormattedText().toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] != '§')
                continue;
            char colorKey = chars[i + 1];
            switch (colorKey) {
                case '0':   return new Color(0, 0, 0);

                case '1':   return new Color(0, 0, 170);

                case '2':   return new Color(0, 170, 0);

                case '3':   return new Color(0, 170, 170);

                case '4':   return new Color(170, 0, 0);

                case '5':   return new Color(170, 0 ,170);

                case '6':   return new Color(255, 170, 0);

                case '7':   return new Color(170, 170, 170);

                case '8':   return new Color(85, 85, 85);

                case '9':   return new Color(85, 85, 255);

                case 'a':   return new Color(85, 255, 85);

                case 'b':   return new Color(85, 255, 255);

                case 'c':   return new Color(255, 85, 85);

                case 'd':   return new Color(255, 85, 255);

                case 'e':   return new Color(255, 255, 85);
            }
            break;
        }
        return new Color(Integer.MAX_VALUE);
    }
}
