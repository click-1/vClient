package com.vClient.module.targets;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Invisible extends Module {
    public Invisible() {
        super("Invisible", Keyboard.CHAR_NONE, Category.TARGETS, null);
    }
}
