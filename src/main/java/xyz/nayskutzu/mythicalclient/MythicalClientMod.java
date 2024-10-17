package xyz.nayskutzu.mythicalclient;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.nayskutzu.mythicalclient.data.MemoryStorageDriveData;

import xyz.nayskutzu.mythicalclient.utils.ChatColor;
import xyz.nayskutzu.mythicalclient.utils.Config;
import xyz.nayskutzu.mythicalclient.v2.WebServer;
import net.minecraftforge.fml.common.Loader;

import net.minecraft.client.settings.KeyBinding;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.realmsclient.gui.ChatFormatting;

@Mod(modid = "mythicalclient", clientSideOnly = true, useMetadata = true)
public class MythicalClientMod {
    public static boolean ToggleSneak = false;
    private static final Logger LOGGER = LogManager.getLogger();
    public static MythicalClientMod instance = new MythicalClientMod();
    public static KeyBinding KeyBindSafewalk;
    private static Config config;
    private boolean toggled = false;
    public static int port;
    public static MemoryStorageDriveData data = new MemoryStorageDriveData();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        config.updateConfig(event.getSuggestedConfigurationFile(), true);
        port = 9865;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            while (isPortInUse(port)) {
                port++;
            }
            WebServer webServer = new WebServer(port);
            webServer.start();
            while (!webServer.wasStarted()) {
                LOGGER.info("Web server is starting...");
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Web server started on port " + port);
        LOGGER.info("MythicalClient is initialized");
        WindowState.updateTitle("MythicalClient | ChillLoader (1.8.9)");
        WindowState.UpdateIcon();
        MythicalClientMod.data.put("name", "NaysKutzu");
        MythicalClientMod.data.put("uuid", "PLM");
        MythicalClientMod.data.put("version", "1.8.9");
        
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:" + port));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void sendHelp() {
        this.sendChat(ChatFormatting.LIGHT_PURPLE + "MythicalClient Help (SafeWalk)");
        this.sendChat(ChatFormatting.GRAY + "Commands:");
        this.sendChat(ChatFormatting.GRAY + "/safewalk mode - Change the mode");
        this.sendChat(ChatFormatting.GRAY + "/safewalk chat - Toggle chat messages");
        this.sendChat(ChatFormatting.GRAY + "/safewalk click - Toggle auto-click");
        this.sendChat(ChatFormatting.GRAY + "/safewalk fall - Toggle auto-disable on fall");
        this.sendChat(ChatFormatting.GRAY + "/safewalk jump - Toggle auto-disable on jump");
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("ToggleSneak")) {
            ToggleSneak = true;
        }
    }

    @Mod.EventHandler
    public void FMLModDisabledEvent(FMLModDisabledEvent event) {
        config.saveConfig();
    }

    public static void sendMessageToChat(String message, boolean raw) {
        String playerName = net.minecraft.client.Minecraft.getMinecraft().thePlayer.getName();
        String formattedMessage = message.replace("%player%", playerName);
        if (!raw) {
            formattedMessage = "&7[&5&lMythical&d&lClient&7] ➡ " + formattedMessage;
        }
        net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(
                ChatColor.translateAlternateColorCodes('&', formattedMessage)));
    }

    public void sendChat(String message) {
        if (MythicalClientMod.config.chat || message.contains("Chat Messages") || message.contains("for help.")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public void sendToggle(String name, String good, String bad, boolean isGood) {

        this.sendChat(ChatFormatting.GRAY + "[" + ChatFormatting.DARK_PURPLE + name + ChatFormatting.GRAY + "]"
                + ChatFormatting.WHITE + " toggled "
                + (isGood ? ChatFormatting.GREEN + good : ChatFormatting.RED + bad));
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void toggle(boolean toggled) {
        this.toggled = toggled;
    }

    public void toggle() {
        this.toggle(!this.isToggled());
    }

    static {
        config = Config.instance;
        data = new MemoryStorageDriveData();
    }

    /**
     * Check if a port is in use
     * 
     * @param port The port to check
     * 
     * @return boolean
     */
    public static boolean isPortInUse(int port) {
        try {
            (new java.net.ServerSocket(port)).close();
            return false;
        } catch (java.io.IOException e) {
            return true;
        }
    }

}
