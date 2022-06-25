package com.vClient.module.targets;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Animal extends Module {
    public Animal() {
        super("Animal", Keyboard.CHAR_NONE, Category.TARGETS, null);
    }
}
