package com.vClient.module.player;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak", Keyboard.CHAR_NONE, Category.PLAYER);
    }
}
