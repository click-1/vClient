package com.vClient.module;

import com.vClient.module.combat.*;
import com.vClient.module.misc.*;
import com.vClient.module.movement.*;
import com.vClient.module.player.*;
import com.vClient.module.render.*;
import com.vClient.util.MyTrie;
import com.vClient.util.custom_font.CustomFontUtil;

import java.util.*;

public class ModuleManager {
    public MyTrie trie;
    private HashMap<String, Module> moduleMap = new HashMap<>();
    private ArrayList<Module> moduleList = new ArrayList<>();
    private ArrayList<Module> moduleList2 = new ArrayList<>();

    public ModuleManager() {
        //COMBAT
        moduleMap.put("AntiBot", new AntiBot());
        moduleMap.put("KillAura", new KillAura());
        moduleMap.put("Reach", new Reach());
        //moduleMap.put("TargetStrafe", new TargetStrafe());
        //moduleMap.put("WTap", new WTap());
        //MOVEMENT
        moduleMap.put("Fly", new Fly());
        moduleMap.put("Freeze", new Freeze());
        moduleMap.put("InvMove", new InvMove());
        moduleMap.put("LongJump", new LongJump());
        moduleMap.put("NoSlow", new NoSlow());
        moduleMap.put("Speed", new Speed());
        moduleMap.put("Sprint", new Sprint());
        //RENDER
        moduleMap.put("Camera", new Camera());
        moduleMap.put("ClickGUI", new ClickGUI());
        moduleMap.put("ESP", new ESP());
        moduleMap.put("FullBright", new FullBright());
        moduleMap.put("StableFOV", new StableFOV());
        moduleMap.put("TargetHUD", new TargetHUD());
        //PLAYER
        moduleMap.put("AutoClicker", new AutoClicker());
        moduleMap.put("AutoTool", new AutoTool());
        moduleMap.put("FastBreak", new FastBreak());
        moduleMap.put("FastPlace", new FastPlace());
        moduleMap.put("InvManager", new InvManager());
        moduleMap.put("NoFall", new NoFall());
        moduleMap.put("Velocity", new Velocity());
        //MISC
        moduleMap.put("Disabler", new Disabler());
        moduleMap.put("GameSpeed", new GameSpeed());

        moduleList.addAll(moduleMap.values());
        moduleList2.addAll(moduleMap.values());
        moduleList.sort(Comparator.comparing(m -> m.getName()));
        moduleList2.sort(Comparator.comparingDouble(m -> CustomFontUtil.arial.getStringWidth(((Module) m).getName())).reversed());
        Set<String> cleanedStrings = new HashSet<>();
        for (Module m : moduleList)
            cleanedStrings.add(cleanString(m.getName()));
        trie = new MyTrie(cleanedStrings);
    }
    public ArrayList<Module> getModules() {
        return moduleList;
    }
    public ArrayList<Module> getModulesByLength() {
        return moduleList2;
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
