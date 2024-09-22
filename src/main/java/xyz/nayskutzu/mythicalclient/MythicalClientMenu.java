
package xyz.nayskutzu.mythicalclient;

import javax.swing.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import net.minecraft.client.Minecraft;
import xyz.nayskutzu.mythicalclient.ui.BridgeHack;
import xyz.nayskutzu.mythicalclient.ui.NukeProcess;
import xyz.nayskutzu.mythicalclient.ui.PlayerESP;
import xyz.nayskutzu.mythicalclient.ui.SendMessageInGame;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

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
            frame.setSize(1600, 600);

            // Create a modern look and feel
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Main panel with modern design
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(45, 45, 45));

            // Text area with modern design
            JTextArea textArea = new JTextArea();
            textArea.setText(
                    " Welcome to MythicalClient\n\n This is a Minecraft mod that adds various features to the game.\n\n This is itself an injector and a executor that lets you play with the game components while you are inside it!\n\n If you are a developer, you can write and execute your own code here.\n\n Enjoy the power of MythicalClient!\n\n Also avoid using this in multiplayer servers as it may lead to a ban if caught.\n\n The force op exploit works only on servers that are vulnerable to it.\n\n Requirements are BungeeCord servers with SafeNET or BungeeGuard.\n\n I can't guarantee that this will work on all servers, but it's worth a try.\n\n Good luck!\n\n And for the nerds out there, this exploit is based on a memory leak into Protocol LIB!\n\n");
            textArea.setEditable(false);
            textArea.setBackground(new Color(30, 30, 30));
            textArea.setForeground(new Color(144, 238, 144));
            textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Button panel with modern design
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(0, 1, 10, 10)); // One column, multiple rows with spacing
            buttonPanel.setBackground(new Color(45, 45, 45));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            String[] buttonLabels = { "Force OP (Protocol LIB)", "PlayerESP","SafeWalk","Send Chat Message","Nuke", "Kill Game","Execute" };
            for (String label : buttonLabels) {
                JButton button = new JButton(label);
                button.setBackground(new Color(30, 30, 30));
                button.setForeground(new Color(144, 238, 144));
                button.setFocusPainted(false);
                button.setFont(new Font("Monospaced", Font.BOLD, 12));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(144, 238, 144)),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                buttonPanel.add(button);
            }

            ((JButton) buttonPanel.getComponent(5)).addActionListener(e -> Minecraft.getMinecraft().shutdown());


            ((JButton) buttonPanel.getComponent(0)).addActionListener(e -> MythicalClientMenu.forceOP());
            ((JButton) buttonPanel.getComponent(1)).addActionListener(e -> PlayerESP.main());
            ((JButton) buttonPanel.getComponent(2)).addActionListener(e -> BridgeHack.main());
            ((JButton) buttonPanel.getComponent(3)).addActionListener(e -> SendMessageInGame.main());
            
            ((JButton) buttonPanel.getComponent(4)).addActionListener(e -> NukeProcess.main());
            ((JButton) buttonPanel.getComponent(6)).addActionListener(e -> {
                JFrame promptFrame = new JFrame("Execute Code");
                promptFrame.setSize(400, 300);
                promptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTextArea codeArea = new JTextArea();
                codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(codeArea);

                JButton runButton = new JButton("Run");
                runButton.setBackground(new Color(30, 30, 30));
                runButton.setForeground(new Color(144, 238, 144));
                runButton.setFocusPainted(false);
                runButton.setFont(new Font("Monospaced", Font.BOLD, 12));
                runButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(144, 238, 144)),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                runButton.addActionListener(event -> {
                    String code = codeArea.getText();
                    try {
                        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
                        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                        JavaFileObject file = new SimpleJavaFileObject(URI.create("string:///DynamicCode.java"),
                                JavaFileObject.Kind.SOURCE) {
                            @Override
                            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                                return code;
                            }
                        };

                        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
                        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
                                compilationUnits);

                        if (task.call()) {
                            URLClassLoader classLoader = URLClassLoader
                                    .newInstance(new URL[] { new File("").toURI().toURL() });
                            Class<?> cls = Class.forName("DynamicCode", true, classLoader);
                            Method main = cls.getDeclaredMethod("main", String[].class);
                            main.invoke(null, (Object) new String[] {});
                        } else {
                            StringBuilder errorMsg = new StringBuilder();
                            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                                errorMsg.append(diagnostic.toString()).append("\n");
                            }
                            throw new Exception("Compilation failed:\n" + errorMsg.toString());
                        }
                        JOptionPane.showMessageDialog(promptFrame, "Code executed successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(promptFrame, "Error executing code: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }

                    promptFrame.dispose();
                });

                promptFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
                promptFrame.getContentPane().add(runButton, BorderLayout.SOUTH);
                promptFrame.setVisible(true);
            });

            mainPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.WEST);

            frame.getContentPane().add(mainPanel);
            frame.setVisible(true);
        });
    }

    private static void forceOP() {
        System.out.println("Forcing OP using Protocol LIB...");
        MythicalClientMod.sendMessageToChat("&7[Server: Opped %player%]");
    }

}
