package com.vClient.module.movement;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Keyboard.CHAR_NONE, Category.MOVEMENT);
    }
}
