package com.vClient.module.visual;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Keyboard.CHAR_NONE, Category.VISUAL, "Remove camera shaking effect when hurt.");
    }
}
