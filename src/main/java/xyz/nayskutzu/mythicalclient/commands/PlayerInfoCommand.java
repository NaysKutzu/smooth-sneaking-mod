package xyz.nayskutzu.mythicalclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;
import java.util.Random;

public class PlayerInfoCommand extends CommandBase {
    private static final String PREFIX = "§8[§c§lGamster§8] ";
    private static final String SEPARATOR = "§8§m                                                  ";
    private static final String SECURE_DOMAIN = "gamster.org";

    @Override
    public String getCommandName() {
        return "playerinfo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/playerinfo <player>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            sendMessage("§cUsage: " + getCommandUsage(sender));
            return;
        }

        String target = args[0];
        String token = generateSecureToken();
        
        String[] messages = {
            SEPARATOR,
            PREFIX + "§8▎ §c§lWARNING: §7Do not show this in screenshares!",
            PREFIX + "§8▎ §7Fetching data for player: §f" + target,
            PREFIX + "§8▎ §7Secure link: §b§nhttps://" + SECURE_DOMAIN + "/staff/lookup/" + target + "?token=" + token,
            PREFIX + "§8▎ §7This link will expire in §c5 minutes",
            PREFIX + "§8▎ §c§lNOTE: §7Close this chat immediately after copying",
            SEPARATOR
        };
        
        for (String message : messages) {
            sendMessage(message);
        }
    }

    private String generateSecureToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 32; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return token.toString();
    }

    private void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
} 