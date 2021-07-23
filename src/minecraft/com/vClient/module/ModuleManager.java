package com.vClient.module;

import com.vClient.module.combat.*;
import com.vClient.module.movement.*;
import com.vClient.module.player.*;
import com.vClient.module.render.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ModuleManager {
    private ArrayList<Module> modules = new ArrayList<>();
    public MyTrie trie;

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
        modules.add(new TargetHUD());
        //PLAYER
        modules.add(new Autoclicker());
        modules.add(new FastBreak());
        modules.add(new FastPlace());
        modules.add(new NoFall());
        modules.add(new Velocity());
        //MISC

        modules.sort(Comparator.comparing(m -> m.getName()));
        Set<String> cleanedStrings = new HashSet<>();
        for (Module m : modules)
            cleanedStrings.add(cleanString(m.getName()));
        trie = new MyTrie(cleanedStrings);
    }
    public ArrayList<Module> getModules() {
        return modules;
    }
    public Module getModulebyName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }
    public ArrayList<String> getModuleList() {
        ArrayList<String> ans = new ArrayList<>();
        for (Module m : modules)
            ans.add(m.getName());
        return ans;
    }
}
