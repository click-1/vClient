package com.vClient.module;

import com.vClient.module.combat.*;
import com.vClient.module.movement.*;
import com.vClient.module.player.*;
import com.vClient.module.render.ClickGUI;

import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        //COMBAT
        modules.add(new Killaura());
        modules.add(new Antibot());
        modules.add(new Reach());
        //MOVEMENT
        modules.add(new Sprint());
        modules.add(new Fly());
        modules.add(new NoSlow());
        modules.add(new LongJump());
        //RENDER
        modules.add(new ClickGUI());
        //PLAYER
        modules.add(new NoFall());
        //MISC

    }
    public ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModulebyName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
