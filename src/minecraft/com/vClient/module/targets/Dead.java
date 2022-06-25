package com.vClient.module.targets;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Dead extends Module {
    public Dead() {
        super("Dead", Keyboard.CHAR_NONE, Category.TARGETS, null);
    }
}
