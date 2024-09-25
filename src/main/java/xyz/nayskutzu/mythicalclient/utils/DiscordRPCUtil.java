package xyz.nayskutzu.mythicalclient.utils;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordRPCUtil {
    public long start;
    public DiscordRPC rpc = DiscordRPC.INSTANCE;
    private DiscordEventHandlers handlers;
    public boolean initialized = false;

    public void init() {

        handlers = new DiscordEventHandlers();

        handlers.ready = (user) -> System.out.println("Ready!");
        start = System.currentTimeMillis();

        rpc.Discord_Initialize("1007992865303052319", handlers, true, "");

        initialized = true;

    }

    public static DiscordRPCUtil instance() {

        return new DiscordRPCUtil();

    }

    public static void update(String state, String details, String s, String ss) {

        DiscordRPCUtil rpc1 = new DiscordRPCUtil();

        if(!rpc1.initialized)
            rpc1.init();

        DiscordRichPresence richPresence = new DiscordRichPresence();
        richPresence.state = state;
        richPresence.details = details;
        richPresence.largeImageKey = "logo";
        richPresence.startTimestamp = rpc1.start;

        rpc1.rpc.Discord_UpdatePresence(richPresence);

    }
}
