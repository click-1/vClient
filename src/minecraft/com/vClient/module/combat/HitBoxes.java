package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class HitBoxes extends Module {
    public HitBoxes() {
        super("HitBoxes", Keyboard.CHAR_NONE, Category.COMBAT, "Enlarge hitboxes of players.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        
    }
}
