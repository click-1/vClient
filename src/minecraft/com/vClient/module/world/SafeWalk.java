package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventMove;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", Keyboard.CHAR_NONE, Category.WORLD, "Prevent player from falling off the edge of blocks.");
        this.setDisplayNotif(true);
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (mc.thePlayer.onGround)
            event.setSafeWalk(true);
    }
}
