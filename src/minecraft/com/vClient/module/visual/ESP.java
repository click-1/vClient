package com.vClient.module.visual;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event3D;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.module.minigames.MMDetector;
import com.vClient.util.RenderUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import java.awt.*;
import java.util.ArrayList;

public class ESP extends Module {
    private String mode;

    public ESP() {
        super("ESP", Keyboard.CHAR_NONE, Category.VISUAL, "Extrasensory perception of entities.");
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Box");
        options.add("Outline");
        updateDisplay("Box");
        vClient.instance.settingsManager.rSetting(new Setting("ESP Mode", this, "Box", options));
    }

    @EventTarget
    public void on3D(Event3D event) {
        mode = vClient.instance.settingsManager.getSettingByName("ESP Mode").getValString();
        updateDisplay(mode);
        this.setFullDisplayName(this.getName() + " " + this.getDisplayMode());

        for (final Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && entity != mc.thePlayer && RenderUtil.canDisplay((EntityLivingBase) entity) && mc.thePlayer.getDistanceToEntity(entity) >= 1F) {
                final EntityLivingBase entityLiving = (EntityLivingBase) entity;
                Color color;
                MMDetector mmDetector = (MMDetector) vClient.instance.moduleManager.getModulebyName("MMDetector");
                if (mmDetector.isToggled() && mmDetector.murderers.contains(entity.getEntityId()))
                    color = new Color(255, 85, 85);
                else if (mmDetector.isToggled() && mmDetector.detectives.contains(entity.getEntityId()))
                    color = new Color(85, 255, 255);
                else
                    color = getColor(entityLiving);

                RenderUtil.drawEntityBox(entity, color, mode.equalsIgnoreCase("Outline"));
            }
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
