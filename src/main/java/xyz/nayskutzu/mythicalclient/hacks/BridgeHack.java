package xyz.nayskutzu.mythicalclient.hacks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;
import xyz.nayskutzu.mythicalclient.commands.SafeWalkCommand;
import xyz.nayskutzu.mythicalclient.event.SafeWalk;

public class BridgeHack {
    public static boolean enabled;

    public static void main() {
        System.out.println("Bridge hack started...");
        // Bridge hack code here
        if (enabled) {
            enabled = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(SafeWalk.instance);
        } else {
            enabled = true;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(SafeWalk.instance);
            MythicalClientMod.KeyBindSafewalk = new KeyBinding("Toggle Safewalk", 47, "Safewalk");
            ClientRegistry.registerKeyBinding(MythicalClientMod.KeyBindSafewalk);
            ClientCommandHandler.instance.registerCommand(SafeWalkCommand.instance);
        }
        System.out.println("Bridge hack completed.");
    }

}
