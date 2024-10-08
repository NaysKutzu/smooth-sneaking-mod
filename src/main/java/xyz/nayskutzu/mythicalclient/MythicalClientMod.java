package xyz.nayskutzu.mythicalclient;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.nayskutzu.mythicalclient.data.MemoryStorageDriveData;
import xyz.nayskutzu.mythicalclient.listeners.KeyListener;
import xyz.nayskutzu.mythicalclient.listeners.RenderListener;
import xyz.nayskutzu.mythicalclient.utils.ChatColor;
import xyz.nayskutzu.mythicalclient.utils.Config;
import xyz.nayskutzu.mythicalclient.v2.WebServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

import net.minecraft.client.settings.KeyBinding;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.realmsclient.gui.ChatFormatting;

@Mod(modid = "mythicalclient", clientSideOnly = true, useMetadata = true)
public class MythicalClientMod {
    // private final Minecraft mc = Minecraft.getMinecraft();
    public static boolean ToggleSneak = false;
    private static final Logger LOGGER = LogManager.getLogger();
    public static MythicalClientMod instance = new MythicalClientMod();
    public static KeyBinding KeyBindSafewalk;
    private static Config config;
    private boolean toggled = false;
    public static int port;
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static MemoryStorageDriveData data = new MemoryStorageDriveData();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        config.updateConfig(event.getSuggestedConfigurationFile(), true);
        port = 9865;
        //DiscordRPCUtil.update("MythicalClient", "In the main menu!", "", "");
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
        MinecraftForge.EVENT_BUS.register(new KeyListener());
        MinecraftForge.EVENT_BUS.register(new RenderListener());
        LOGGER.info("MythicalClient is initialized");
        WindowState.updateTitle("MythicalClient | ChillLoader (1.8.9)");
        WindowState.UpdateIcon();
        MythicalClientMenu.main();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        try {
            scheduler.schedule(() -> {

                try {
                    while (mc.thePlayer == null) {
                        try {
                            Thread.sleep(1000); // Wait for 1 second before checking again
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    MythicalClientMod.data.put("name", mc.thePlayer.getName());
                    MythicalClientMod.data.put("uuid", mc.thePlayer.getUniqueID().toString());
                    MythicalClientMod.data.put("version", "1.8.9");
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:" + port));
                        MythicalClientMenu.frame.setVisible(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    MythicalClientMod.data.put("name", "Unknown");
                    e.printStackTrace();
                }
            }, 3, TimeUnit.SECONDS); // Adjust the delay as needed
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendHelp() {
        this.sendChat(ChatFormatting.LIGHT_PURPLE + "MythicalClient Help");
        this.sendChat(ChatFormatting.YELLOW + "Commands:");
        this.sendChat(ChatFormatting.YELLOW + "/safewalk mode - Change the mode");
        this.sendChat(ChatFormatting.YELLOW + "/safewalk chat - Toggle chat messages");
        this.sendChat(ChatFormatting.YELLOW + "/safewalk click - Toggle auto-click");
        this.sendChat(ChatFormatting.YELLOW + "/safewalk fall - Toggle auto-disable on fall");
        this.sendChat(ChatFormatting.YELLOW + "/safewalk jump - Toggle auto-disable on jump");
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
