package xyz.nayskutzu.mythicalclient.hacks;

import javax.swing.JOptionPane;

import xyz.nayskutzu.mythicalclient.MythicalClientMod;

public class SendMessageInGame {
    public static void main() {
        String message = JOptionPane.showInputDialog(null, "Enter your message:");
        if (message != null) {
            System.out.println("Message: " + message);
            MythicalClientMod.sendMessageToChat(message);
        } else {
            System.out.println("No message entered.");
        }
    }
}
