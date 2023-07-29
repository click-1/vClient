package com.vClient.module.visual;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event3D;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ItemESP extends Module {
    final Color color = new Color(255, 255, 85);
    public ItemESP() {
        super("ItemESP", Keyboard.CHAR_NONE, Category.VISUAL, "Extrasensory perception of dropped gold ingots.");
    }

    @EventTarget
    public void on3D(Event3D event) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) entity;
                Item item = entityItem.getEntityItem() != null ? entityItem.getEntityItem().getItem() : null;
                if (item == Items.gold_ingot)
                    RenderUtil.drawEntityBox(entity, color, false);
            }
        }
    }
}
