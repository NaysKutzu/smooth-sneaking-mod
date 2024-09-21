package xyz.nayskutzu.mythicalclient;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import xyz.nayskutzu.mythicalclient.utils.ChatColor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "mythicalclient", clientSideOnly = true, useMetadata = true)
public class MythicalClientMod {

    private static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("MythicalClient is initialized");
        WindowState.updateTitle("MythicalClient | LiteLoader (1.8.9)");
        WindowState.UpdateIcon();
        MythicalClientMenu.main();
    }

    public static void sendMessageToChat(String message) {
        LOGGER.info("Sending message to chat: " + message);
        String playerName = net.minecraft.client.Minecraft.getMinecraft().thePlayer.getName();
        String formattedMessage = message.replace("%player%", playerName);
        net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(ChatColor.translateAlternateColorCodes('&', formattedMessage)));
    }
}
