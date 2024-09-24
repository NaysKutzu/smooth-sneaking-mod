package xyz.nayskutzu.mythicalclient.hacks;

import java.util.ArrayList;
import java.util.List;

public class IModList {
    private static List<String> modList = new ArrayList<>();

    public static List<String> getModList() {
        return new ArrayList<>(modList);
    }

    public static void addMod(String modName) {
        if (!modList.contains(modName)) {
            modList.add(modName);
            System.out.println("Added mod: " + modName);
        } else {
            System.out.println("Mod already exists: " + modName);
        }
    }

    public static void removeMod(String modName) {
        if (modList.contains(modName)) {
            modList.remove(modName);
            System.out.println("Removed mod: " + modName);
        } else {
            System.out.println("Mod not found: " + modName);
        }
    }
}
