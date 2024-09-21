
package xyz.nayskutzu.mythicalclient;

import javax.swing.*;
import java.awt.*;

public class MythicalClientMenu {

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
            JFrame frame = new JFrame("MythicalClient");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);

            JTextArea textArea = new JTextArea();
            textArea.setText("Welcome to MythicalClient\n\nThis is a Minecraft mod that adds various features to the game.");
            textArea.setEditable(false);
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.GREEN);
            textArea.setFont(new Font("Monospaced", Font.BOLD, 14));

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(0, 1)); // One column, multiple rows
            buttonPanel.setBackground(Color.DARK_GRAY);

            String[] buttonLabels = {"Force OP (Protocol LIB)", "Live Tracers", "Bridge Hack"};
            for (String label : buttonLabels) {
                JButton button = new JButton(label);
                button.setBackground(Color.BLACK);
                button.setForeground(Color.GREEN);
                button.setFocusPainted(false);
                button.setFont(new Font("Monospaced", Font.BOLD, 12));
                buttonPanel.add(button);
            }  

            ((JButton) buttonPanel.getComponent(0)).addActionListener(e -> forceOP());
            ((JButton) buttonPanel.getComponent(1)).addActionListener(e -> System.out.println("Live Tracers enabled"));
            ((JButton) buttonPanel.getComponent(2)).addActionListener(e -> System.out.println("Bridge Hack enabled"));


            // Add Execute button
            JButton executeButton = new JButton("Execute");
            executeButton.setBackground(Color.BLACK);
            executeButton.setForeground(Color.GREEN);
            executeButton.setFocusPainted(false);
            executeButton.setFont(new Font("Monospaced", Font.BOLD, 12));
            executeButton.addActionListener(e -> {
                JFrame promptFrame = new JFrame("Execute Code");
                promptFrame.setSize(400, 300);
                promptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTextArea codeArea = new JTextArea();
                codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(codeArea);

                JButton runButton = new JButton("Run");
                runButton.addActionListener(event -> {
                    String code = codeArea.getText();
                    code = code.replace("\n", "").replace("\r", "");
                    System.out.println("Executing code: " + code);
                    MythicalClientMod.sendMessageToChat(code);
                    promptFrame.dispose();
                });

                promptFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
                promptFrame.getContentPane().add(runButton, BorderLayout.SOUTH);
                promptFrame.setVisible(true);
            });
            buttonPanel.add(executeButton);

            frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.WEST);
            frame.setVisible(true);
        });
    }

    private static void forceOP() {
        System.out.println("Forcing OP using Protocol LIB...");
        // Code to force OP using Protocol LIB
        MythicalClientMod.sendMessageToChat("&7[Server: Opped %player%]");

    }

}
