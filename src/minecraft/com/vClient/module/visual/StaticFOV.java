package com.vClient.module.visual;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class StaticFOV extends Module {
    public StaticFOV() {
        super("StaticFOV", Keyboard.CHAR_NONE, Category.VISUAL, "Remove dynamic FOV effect.");
    }
}
