package com.vClient.command.commands;

import com.vClient.command.Command;
import com.vClient.module.Module;
import com.vClient.vClient;

public class Panic extends Command {
    public Panic() {
        super("Panic", "Deactivate all active modules", ".panic", "p");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 0)
            return;
        for (Module m : vClient.instance.moduleManager.getModules()) {
            if (m.isToggled())
                m.toggle();
        }
        vClient.addChatMessage("Closed all modules.");
    }
}
