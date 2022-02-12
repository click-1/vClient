package com.vClient.module.combat;

import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class KeepSprint extends Module {
    public KeepSprint() {
        super("KeepSprint", Keyboard.CHAR_NONE, Category.COMBAT, "Maintain sprint while in combat.");
    }
}
