package com.vClient.module.targets;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Mob extends Module {
    public Mob() {
        super("Mob", Keyboard.CHAR_NONE, Category.TARGETS, null);
    }
}
