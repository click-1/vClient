package com.vClient.module.render;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class StableFOV extends Module {
    public StableFOV() {
        super("StableFOV", Keyboard.CHAR_NONE, Category.RENDER, "Remove dynamic FOV effect.");
    }
}
