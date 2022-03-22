package com.vClient.module.visual;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventClairvoyance;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Clairvoyance extends Module {
    public Clairvoyance() {
        super("Clairvoyance", Keyboard.CHAR_NONE, Category.VISUAL, "See entities through blocks.");
    }

    @EventTarget
    public void onClairvoyance(EventClairvoyance event) {
        switch (event.getState()) {
            case PRE:   GL11.glEnable(32823);
                        GL11.glPolygonOffset(1f, -10000000f);
                        break;
            case POST:  GL11.glPolygonOffset(1f, 10000000f);
                        GL11.glDisable(32823);
                        break;
        }
    }
}
