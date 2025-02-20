package xyz.nayskutzu.mythicalclient.utils;

import java.util.HashSet;
import java.util.Set;

public class FriendlyPlayers {
    private static final Set<String> friendlyPlayers = new HashSet<>();

    public static boolean isFriendly(String playerName) {
        return friendlyPlayers.contains(playerName);
    }

    public static void addFriend(String playerName) {
        friendlyPlayers.add(playerName);
    }

    public static void removeFriend(String playerName) {
        friendlyPlayers.remove(playerName);
    }

    public static Set<String> getFriendsList() {
        return new HashSet<>(friendlyPlayers);
    }
} 