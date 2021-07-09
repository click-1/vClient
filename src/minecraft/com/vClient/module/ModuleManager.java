package com.vClient.module;

import com.vClient.module.combat.*;
import com.vClient.module.movement.*;
import com.vClient.module.player.*;
import com.vClient.module.render.*;

import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        //COMBAT
        modules.add(new Antibot());
        modules.add(new Killaura());
        modules.add(new Reach());
        //modules.add(new TargetStrafe());
        //MOVEMENT
        modules.add(new Fly());
        modules.add(new InventoryMove());
        modules.add(new LongJump());
        modules.add(new NoSlow());
        modules.add(new Sprint());
        //RENDER
        modules.add(new ClickGUI());
        modules.add(new FullBright());
        modules.add(new StableFOV());
        //PLAYER
        modules.add(new Autoclicker());
        modules.add(new FastBreak());
        modules.add(new FastPlace());
        modules.add(new NoFall());
        modules.add(new Velocity());
        //MISC

    }
    public ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModulebyName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
