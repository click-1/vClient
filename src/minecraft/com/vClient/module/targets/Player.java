package com.vClient.module.targets;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Player extends Module {
    public Player() {
        super("Player", Keyboard.CHAR_NONE, Category.TARGETS, null);
    }
}
