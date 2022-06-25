package com.vClient.module.targets;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Villager extends Module {
    public Villager() {
        super("Villager", Keyboard.CHAR_NONE, Category.TARGETS, null);
    }
}
