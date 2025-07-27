package xyz.nayskutzu.mythicalclient.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.multiplayer.WorldClient;

public class BanMeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "banme";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/banme <reason>";
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

        String reason = String.join(" ", args);
        disconnectWithBanScreen(reason);
    }

    private void disconnectWithBanScreen(final String reason) {
        final Minecraft mc = Minecraft.getMinecraft();
        
        // First disconnect from the server
        if (mc.theWorld != null && mc.thePlayer != null) {
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld((WorldClient)null);
        }

        // Then show the ban screen after a short delay
        new Thread(() -> {
            try {
                Thread.sleep(500); // Wait half a second
                mc.addScheduledTask(() -> {
                    IChatComponent message = new ChatComponentText(
                        "§7⊲ §c§lGamster.org §7⊳\n" +
                        "\n§cYou were §4§lPERMANENTLY§r§c banned from Gamster Network.\n" +
                        "§7You were banned for §8⋙ §e" + reason + "\n" +
                        "\n§7You can appeal this ban at §bhttps://unban.gamster.org"
                    );
                    mc.displayGuiScreen(new GuiDisconnected(new GuiMainMenu(), "connect.failed", message));
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
} 