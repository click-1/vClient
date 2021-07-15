package com.vClient.discord;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRP {
    private boolean running = true;
    private long created = 0;
    public void start() {
        created = System.currentTimeMillis();
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> {
            System.out.println("Emperor " + user.username + "#" + user.discriminator + " has arrived!");
            update("Injecting vClient...", "");
        }).build();

        DiscordRPC.discordInitialize("864625157967314955", handlers, true);
        new Thread("DiscordRPC Callback") {
            @Override
                public void run() {
                while (running)
                    DiscordRPC.discordRunCallbacks();
            }
        }.start();
    }
    public void shutdown() {
        running = false;
        DiscordRPC.discordShutdown();
    }
    public void update(String firstLine, String secondLine) {
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder(secondLine);
        builder.setBigImage("large", "");
        builder.setDetails(firstLine);
        builder.setStartTimestamps(created);
        DiscordRPC.discordUpdatePresence(builder.build());
    }
}
