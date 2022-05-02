package com.vClient.module;

import com.vClient.module.combat.*;
import com.vClient.module.exploit.*;
import com.vClient.module.exploit.Queue;
import com.vClient.module.movement.*;
import com.vClient.module.player.*;
import com.vClient.module.visual.*;
import com.vClient.module.world.*;
import com.vClient.util.MyTrie;

import java.util.*;

public class ModuleManager {
    public MyTrie trie;
    private HashMap<String, Module> moduleMap = new HashMap<>();
    private ArrayList<Module> moduleList = new ArrayList<>();

    public ModuleManager() {
        //COMBAT
        moduleMap.put("AntiBot", new AntiBot());
        moduleMap.put("AutoPot", new AutoPot());
        moduleMap.put("KeepSprint", new KeepSprint());
        moduleMap.put("KillAura", new KillAura());
        moduleMap.put("Reach", new Reach());
        moduleMap.put("TargetHUD", new TargetHUD());
        moduleMap.put("TargetStrafe", new TargetStrafe());
        //moduleMap.put("WTap", new WTap());
        //EXPLOIT
        moduleMap.put("AntiWatchdog", new AntiWatchdog());
        moduleMap.put("Blink", new Blink());
        moduleMap.put("Disabler", new Disabler());
        moduleMap.put("Queue", new Queue());
        moduleMap.put("Rotations", new Rotations());
        moduleMap.put("Spammer", new Spammer());
        //MOVEMENT
        moduleMap.put("Fly", new Fly());
        moduleMap.put("GuiMove", new GuiMove());
        moduleMap.put("LongJump", new LongJump());
        moduleMap.put("NoSlow", new NoSlow());
        moduleMap.put("Speed", new Speed());
        moduleMap.put("Sprint", new Sprint());
        moduleMap.put("Step", new Step());
        //PLAYER
        moduleMap.put("AutoClicker", new AutoClicker());
        moduleMap.put("AutoTool", new AutoTool());
        moduleMap.put("ChestStealer", new ChestStealer());
        moduleMap.put("FastBreak", new FastBreak());
        moduleMap.put("FastPlace", new FastPlace());
        moduleMap.put("InvManager", new InvManager());
        moduleMap.put("Velocity", new Velocity());
        //VISUAL
        moduleMap.put("Camera", new Camera());
        moduleMap.put("Clairvoyance", new Clairvoyance());
        moduleMap.put("ClickGUI", new ClickGUI());
        moduleMap.put("Effects", new Effects());
        moduleMap.put("ESP", new ESP());
        moduleMap.put("NoHurtCam", new NoHurtCam());
        moduleMap.put("StaticFOV", new StaticFOV());
        moduleMap.put("StorageESP", new StorageESP());
        //WORLD
        moduleMap.put("Freeze", new Freeze());
        moduleMap.put("GameSpeed", new GameSpeed());
        moduleMap.put("NoFall", new NoFall());
        moduleMap.put("SafeWalk", new SafeWalk());
        //moduleMap.put("StaffAlert", new StaffAlert());

        moduleList.addAll(moduleMap.values());

        moduleList.sort(Comparator.comparing(Module::getName));
        Set<String> cleanedStrings = new HashSet<>();
        for (Module m : moduleList)
            cleanedStrings.add(cleanString(m.getName()));
        trie = new MyTrie(cleanedStrings);
    }
    public ArrayList<Module> getModules() {
        return moduleList;
    }
    public Module getModulebyName(String name) {
        return moduleMap.get(name);
    }
    public static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }
    public ArrayList<String> getModuleList() {
        return new ArrayList<>(moduleMap.keySet());
    }
}
