
package xyz.nayskutzu.mythicalclient;

import javax.swing.*;
import java.awt.*;


public class MythicalClientMenu {
    public static JFrame frame;

    public static void main() {
        System.out.println("MythicalClient is a Minecraft mod that adds various features to the game.");
        System.out.println("It is developed by Nayskutzu and is available for Minecraft version 1.8.9.");
        System.out.println("The mod is currently in development and new features are being added regularly.");
        displayWindow();
    }
    private static void displayWindow() {
        System.out.println("Displaying the MythicalClient window...");
        // Display the window here
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("MythicalClient");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 230);
            frame.setIconImage(new ImageIcon(MythicalClientMod.class.getResource("/icon16.png")).getImage());
            frame.setVisible(true);
            

            // Create a modern look and feel
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Main panel with modern design
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(45, 45, 45));

            // HTML content
            String htmlContent = "<html>" +
                    "<body style='background-color:#2D2D2D; color:#90EE90; font-family:monospace; padding:10px;'>" +
                    "<h1>Welcome to MythicalClient</h1>" +
                    "<p>The MythicalClient is not yet injected fully.</p>" +
                    "<p>Please join a server to complete the injection process.</p>" +
                    "</body>" +
                    "</html>";

            // JEditorPane to display HTML content
            JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
            editorPane.setEditable(false);
            editorPane.setBackground(new Color(30, 30, 30));
            editorPane.setForeground(new Color(144, 238, 144));
            editorPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            mainPanel.add(new JScrollPane(editorPane), BorderLayout.CENTER);

            frame.getContentPane().add(mainPanel);
        });
    }
}
