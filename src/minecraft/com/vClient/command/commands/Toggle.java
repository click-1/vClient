package com.vClient.command.commands;

import com.vClient.command.Command;
import com.vClient.module.Module;
import com.vClient.vClient;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggle a module", ".toggle <name>", "t");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length == 0) {
            vClient.addChatMessage("\2478Toggle Commands:");
            vClient.addChatMessage(".toggle <module>");
        }
        if (args.length == 1) {
            String moduleName = args[0];
            boolean foundModule = false;
            for (Module m : vClient.instance.moduleManager.getModules()) {
                if (m.getName().equalsIgnoreCase(moduleName)) {
                    m.toggle();
                    vClient.addChatMessage((m.isToggled() ? "Enabled " : "Disabled ") + "\2473" + m.getName());
                    foundModule = true;
                    break;
                }
            }
            if (!foundModule)
                vClient.addChatMessage("Couldn't find module.");
        }
    }
}
