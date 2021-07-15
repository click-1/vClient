package com.vClient.discord;

public class DiscordHandler {
    private static final DiscordHandler INSTANCE = new DiscordHandler();
    private DiscordRP discordRP = new DiscordRP();
    public static final DiscordHandler getInstance() {
        return INSTANCE;
    }
    public void init() {
        discordRP.start();
    }
    public void shutdown() {
        discordRP.shutdown();
    }
    public DiscordRP getDiscordRP() {
        return discordRP;
    }
}
