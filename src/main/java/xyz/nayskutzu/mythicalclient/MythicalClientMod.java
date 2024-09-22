package xyz.nayskutzu.mythicalclient;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.nayskutzu.mythicalclient.utils.ChatColor;
import xyz.nayskutzu.mythicalclient.utils.Config;
import net.minecraftforge.fml.common.Loader;

import net.minecraft.client.settings.KeyBinding;

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

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        config.updateConfig(event.getSuggestedConfigurationFile(), true);
        System.out.println("Loaded config");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("MythicalClient is initialized");
        WindowState.updateTitle("MythicalClient | ChillLoader (1.8.9)");
        WindowState.UpdateIcon();
        MythicalClientMenu.main();
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

    public static void sendMessageToChat(String message) {
        String playerName = net.minecraft.client.Minecraft.getMinecraft().thePlayer.getName();
        String formattedMessage = message.replace("%player%", playerName);
        net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(
                ChatColor.translateAlternateColorCodes('&', formattedMessage)));
    }

    public void sendChat(String message) {
        if (MythicalClientMod.config.chat || message.contains("Chat Messages") || message.contains("for help.")) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public void sendToggle(String name, String good, String bad, boolean isGood) {
        this.sendChat(ChatFormatting.RESET + "[" + ChatFormatting.LIGHT_PURPLE + name + ChatFormatting.RESET + "]"
                + ChatFormatting.YELLOW + " toggled "
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
    }

}
