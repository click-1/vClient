package com.vClient.command.commands;

import com.vClient.command.Command;
import com.vClient.module.Module;
import com.vClient.vClient;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("Bind", "Bind a module to a key", ".bind <module> <key> | <clear> | <list>", "b");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 0) {
            vClient.addChatMessage("\2478Bind Commands:");
            vClient.addChatMessage(".bind <module> <key>");
            vClient.addChatMessage(".bind <clear>");
            vClient.addChatMessage(".bind <list>");
        }
        if (args.length == 2) {
            String moduleName = args[0], keyName = args[1];
            boolean foundModule = false;
            for (Module m : vClient.instance.moduleManager.getModules()) {
                if (m.getName().equalsIgnoreCase(moduleName)) {
                    m.setKey(Keyboard.getKeyIndex(keyName.toUpperCase()));
                    vClient.addChatMessage(String.format("Bound \2473%s \2477to \2476%s", m.getName(), Keyboard.getKeyName(m.getKey())));
                    foundModule = true;
                    break;
                }
            }
            if (!foundModule)
                vClient.addChatMessage("Couldn't find module.");
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (Module m : vClient.instance.moduleManager.getModules())
                    if (!m.getName().equalsIgnoreCase("clickgui"))
                        m.setKey(Keyboard.CHAR_NONE);
                vClient.addChatMessage("Cleared all binds.");
                return;
            }
            if (args[0].equalsIgnoreCase("list")) {
                vClient.addChatMessage("\2478Current binds:");
                for (Module m : vClient.instance.moduleManager.getModules())
                    if (m.getKey() != 0)
                        vClient.addChatMessage(String.format("\2473%s \2477: \2476%s", m.getName(), Keyboard.getKeyName(m.getKey())));
                return;
            }
            vClient.addChatMessage("Invalid bind command.");
        }
    }
}
