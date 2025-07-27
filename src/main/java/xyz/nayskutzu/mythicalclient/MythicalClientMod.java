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
import xyz.nayskutzu.mythicalclient.v2.ChatServer;
import xyz.nayskutzu.mythicalclient.v2.WebServer;
import xyz.nayskutzu.mythicalclient.gui.ToggleGui;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nayskutzu.mythicalclient.commands.*;
import xyz.nayskutzu.mythicalclient.hacks.Aimbot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

@Mod(modid = "mythicalclient", clientSideOnly = true, useMetadata = true)
public class MythicalClientMod {
    public static boolean ToggleSneak = false;
    private static final Logger LOGGER = LogManager.getLogger("[MythicalClient]");
    public static MythicalClientMod instance = new MythicalClientMod();
    public static KeyBinding KeyBindSafewalk;
    public static KeyBinding KeyBindDashboard;
    public static KeyBinding KeyBindPlayerInfo;
    public static KeyBinding KeyBindGroundItems;
    public static KeyBinding KeyBindBedDefence;
    public static KeyBinding KeyBindAimbot;
    public static KeyBinding KeyBindToggleGui;
    private static Config config;
    private boolean toggled = false;
    public static int port;
    public static int chatPort;
    public static MemoryStorageDriveData data = new MemoryStorageDriveData();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        config.updateConfig(event.getSuggestedConfigurationFile(), true);
        port = 9865;
        chatPort = 9866;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            // Register keybindings
            KeyBindPlayerInfo = new KeyBinding("Player Info", Keyboard.KEY_P, "MythicalClient");
            KeyBindGroundItems = new KeyBinding("Ground Items", Keyboard.KEY_G, "MythicalClient");
            KeyBindBedDefence = new KeyBinding("Bed Defence", Keyboard.KEY_B, "MythicalClient");
            KeyBindAimbot = new KeyBinding("Aimbot", Keyboard.KEY_R, "MythicalClient");
            KeyBindToggleGui = new KeyBinding("Toggle GUI", Keyboard.KEY_LCONTROL, "MythicalClient");
            
            ClientRegistry.registerKeyBinding(KeyBindPlayerInfo);
            ClientRegistry.registerKeyBinding(KeyBindGroundItems);
            ClientRegistry.registerKeyBinding(KeyBindBedDefence);
            ClientRegistry.registerKeyBinding(KeyBindAimbot);
            ClientRegistry.registerKeyBinding(KeyBindToggleGui);

            // Start web server in a separate thread to avoid blocking the main thread
            new Thread(() -> {
                try {
                    int attemptPort = port;
                    while (isPortInUse(attemptPort)) {
                        attemptPort++;
                    }
                    port = attemptPort;
                    
                    WebServer webServer = new WebServer(port);
                    webServer.start();
                    LOGGER.info("Web server started on port " + port);
                    
                    MythicalClientMod.data.put("name", "NaysKutzu");
                    MythicalClientMod.data.put("uuid", "PLM");
                    MythicalClientMod.data.put("version", "1.8.9");
                } catch (IOException e) {
                    LOGGER.error("Failed to start web server", e);
                }
            }, "MythicalClient-WebServer").start();
            
            // Start chat server in a separate thread to avoid blocking the main thread
            new Thread(() -> {
                try {
                    int attemptChatPort = chatPort;
                    while (isPortInUse(attemptChatPort)) {
                        attemptChatPort++;
                    }
                    chatPort = attemptChatPort;
                    
                    ChatServer chatServer = new ChatServer(chatPort);
                    chatServer.start();
                    LOGGER.info("Chat server started on port " + chatPort);
                } catch (Exception e) {   
                    LOGGER.error("Failed to start chat server", e);
                }
            }, "MythicalClient-ChatServer").start();
            
            // Register commands
            registerCommands();
            
            // Register event handlers
            MinecraftForge.EVENT_BUS.register(this);
            
            LOGGER.info("MythicalClient is initialized");
        } catch (Exception e) {
            LOGGER.error("Error during initialization", e);
        }
    }

    private void registerCommands() {
        net.minecraftforge.client.ClientCommandHandler commandHandler = 
            net.minecraftforge.client.ClientCommandHandler.instance;
        
        commandHandler.registerCommand(new AntiCheatCommand());
        commandHandler.registerCommand(new FakeStaffCommand());
        commandHandler.registerCommand(new BanMeCommand());
        commandHandler.registerCommand(new DutyCommand());
        commandHandler.registerCommand(new PlayerInfoCommand());
        commandHandler.registerCommand(new ShowStatsCommand());
        commandHandler.registerCommand(new GroundItemFinderCommand());
        commandHandler.registerCommand(new BedDefenceInfoCommand());
    }

    public void sendHelp() {
        this.sendChat(ChatFormatting.LIGHT_PURPLE + "MythicalClient Help (SafeWalk)");
        this.sendChat(ChatFormatting.GRAY + "Commands:");
        this.sendChat(ChatFormatting.GRAY + "/safewalk mode - Change the mode");
        this.sendChat(ChatFormatting.GRAY + "/safewalk chat - Toggle chat messages");
        this.sendChat(ChatFormatting.GRAY + "/safewalk click - Toggle auto-click");
        this.sendChat(ChatFormatting.GRAY + "/safewalk fall - Toggle auto-disable on fall");
        this.sendChat(ChatFormatting.GRAY + "/safewalk jump - Toggle auto-disable on jump");
        this.sendChat(ChatFormatting.GRAY + "/ac - Toggle anticheat");
        this.sendChat(ChatFormatting.GRAY + "/banme - Ban yourself");
        this.sendChat(ChatFormatting.GRAY + "/duty - Toggle duty mode");
        this.sendChat(ChatFormatting.GRAY + "/playerinfo - Get player info");
        this.sendChat(ChatFormatting.GRAY + "/showstats - Get player stats");
        this.sendChat(ChatFormatting.GRAY + "/plinfo - Get player info (P)");
        this.sendChat(ChatFormatting.GRAY + "/grounditems - Get ground items (G)");
        this.sendChat(ChatFormatting.GRAY + "/beddefence - Get bed defence info (B)");
        this.sendChat(ChatFormatting.GRAY + "Press R - Toggle Aimbot (continuous tracking)");
        this.sendChat(ChatFormatting.GRAY + "Press LCONTROL - Toggle GUI (L)");
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
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) {
            LOGGER.debug("Player or world is null, cannot send chat message.");
            return;
        }
        
        String playerName = Minecraft.getMinecraft().thePlayer.getName();
        String formattedMessage = message.replace("%player%", playerName);
        
        if (!raw) {
            formattedMessage = "&7[&5&lMythical&d&lClient&7] ➡ " + formattedMessage;
        }
        
        Minecraft.getMinecraft().thePlayer.addChatMessage(
            new ChatComponentText(ChatColor.translateAlternateColorCodes('&', formattedMessage))
        );
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
        try (java.net.ServerSocket socket = new java.net.ServerSocket(port)) {
            // Port is available
            return false;
        } catch (java.io.IOException e) {
            // Port is in use
            return true;
        }
    }



    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        // Only process on the client phase to reduce redundant processing
        if (event.phase != net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END) {
            return;
        }
        
        // Update aimbot tracking
        Aimbot.update();
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer == null || mc.theWorld == null) return;
        
        if (KeyBindPlayerInfo.isPressed()) {
            // Find closest player
            net.minecraft.entity.player.EntityPlayer closestPlayer = null;
            double closestDistance = Double.MAX_VALUE;
            
            for (Object obj : mc.theWorld.playerEntities) {
                if (obj instanceof net.minecraft.entity.player.EntityPlayer) {
                    net.minecraft.entity.player.EntityPlayer player = (net.minecraft.entity.player.EntityPlayer) obj;
                    if (player != mc.thePlayer) {
                        double distance = mc.thePlayer.getDistanceToEntity(player);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestPlayer = player;
                        }
                    }
                }
            }
            
            if (closestPlayer != null) {
                net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, "plinfo " + closestPlayer.getName());
            } else {
                sendMessageToChat("&c&l[!] &cNo players found nearby!", false);
            }
        }
        
        if (KeyBindGroundItems.isPressed()) {
            net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, "grounditems");
        }
        
        if (KeyBindBedDefence.isPressed()) {
            net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, "beddefence");
        }
        
        if (KeyBindAimbot.isPressed()) {
            Aimbot.toggle();
        }
        
        if (KeyBindToggleGui.isPressed()) {
            mc.displayGuiScreen(new ToggleGui(mc.currentScreen));
        }
        } catch (Exception e) {
            LOGGER.error("Error in key input handler: " + e.getMessage(), e);
        }
    }
}
