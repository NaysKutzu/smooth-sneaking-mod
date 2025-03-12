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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nayskutzu.mythicalclient.commands.AntiCheatCommand;
import xyz.nayskutzu.mythicalclient.commands.FakeStaffCommand;
import xyz.nayskutzu.mythicalclient.commands.BanMeCommand;
import xyz.nayskutzu.mythicalclient.commands.DutyCommand;
import xyz.nayskutzu.mythicalclient.commands.PlayerInfoCommand;
import xyz.nayskutzu.mythicalclient.commands.ShowStatsCommand;
import net.minecraftforge.common.MinecraftForge;
import xyz.nayskutzu.mythicalclient.handlers.ConnectionHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

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
            LOGGER.info("Web server started on port " + port);            
        } catch (IOException e) {
            LOGGER.error("Failed to start web server", e);
        } catch (InterruptedException e) {
            LOGGER.error("Web server startup interrupted", e);
            Thread.currentThread().interrupt();
        }
        
        
        // Register AntiCheat Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new AntiCheatCommand());
        
        // Register FakeStaff Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new FakeStaffCommand());
        
        // Register BanMe Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new BanMeCommand());
        
        // Register Duty Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new DutyCommand());
        
        // Register PlayerInfo Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new PlayerInfoCommand());
        
        // Register ShowStats Command
        net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(new ShowStatsCommand());
        
        // Register Connection Handler
        MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
        
        LOGGER.info("MythicalClient is initialized");
        MythicalClientMod.data.put("name", "NaysKutzu");
        MythicalClientMod.data.put("uuid", "PLM");
        MythicalClientMod.data.put("version", "1.8.9");
        
        MinecraftForge.EVENT_BUS.register(this);
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
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(
                ChatColor.translateAlternateColorCodes('&', formattedMessage)));
        } else {
            LOGGER.warn("Player or world is null, cannot send chat message.");
        }
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

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

    }
}
