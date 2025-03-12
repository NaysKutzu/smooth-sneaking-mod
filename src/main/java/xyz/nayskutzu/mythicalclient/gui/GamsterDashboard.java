package xyz.nayskutzu.mythicalclient.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import xyz.nayskutzu.mythicalclient.gui.components.ColorButton;
import xyz.nayskutzu.mythicalclient.data.MockDataManager;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GamsterDashboard extends GuiScreen {
    private float animationProgress = 0;
    private long openTime;
    private static final int ANIMATION_DURATION = 200;
    
    public GamsterDashboard() {
        this.openTime = System.currentTimeMillis();
    }
    
    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // Left column - Staff Tools
        this.buttonList.add(new ColorButton(1, centerX - 180, centerY - 70, 160, 20, "§c§lAnti-Cheat", 0xFF5555));
        this.buttonList.add(new ColorButton(2, centerX - 180, centerY - 45, 160, 20, "§e§lPlayer Lookup", 0xFFAA00));
        this.buttonList.add(new ColorButton(3, centerX - 180, centerY - 20, 160, 20, "§b§lPunishments", 0x55FFFF));
        this.buttonList.add(new ColorButton(4, centerX - 180, centerY + 5, 160, 20, "§a§lReports", 0x55FF55));
        
        // Right column - Quick Actions
        this.buttonList.add(new ColorButton(5, centerX + 20, centerY - 70, 160, 20, "§d§lVanish", 0xFF55FF));
        this.buttonList.add(new ColorButton(6, centerX + 20, centerY - 45, 160, 20, "§6§lStaff Chat", 0xFF5500));
        this.buttonList.add(new ColorButton(7, centerX + 20, centerY - 20, 160, 20, "§b§lSettings", 0x55FFFF));
        this.buttonList.add(new ColorButton(8, centerX + 20, centerY + 5, 160, 20, "§c§lDisconnect", 0xFF5555));
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Calculate animation
        animationProgress = Math.min(1, (System.currentTimeMillis() - openTime) / (float)ANIMATION_DURATION);
        
        // Draw background with blur
        drawDefaultBackground();
        
        // Draw title
        GlStateManager.pushMatrix();
        float scale = 2.0f * animationProgress;
        GlStateManager.translate(width / 2, 30, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-width / 2, -30, 0);
        drawCenteredString(fontRendererObj, "§c§lGamster Dashboard", width / 2, 30, 0xFFFFFF);
        GlStateManager.popMatrix();
        
        // Draw server stats
        Map<String, Integer> stats = MockDataManager.getServerStats();
        int y = height - 60;
        drawCenteredString(fontRendererObj, String.format(
            "§7Online Players: §f%d §8| §7Staff Online: §f%d §8| §7Active Reports: §f%d §8| §7AC Alerts: §f%d",
            stats.get("Online Players"),
            stats.get("Online Staff"),
            stats.get("Active Reports"),
            stats.get("AC Alerts")
        ), width / 2, y, 0xFFFFFF);
        
        // Draw nearby players
        List<MockDataManager.PlayerData> nearbyPlayers = MockDataManager.getNearbyPlayers();
        if (!nearbyPlayers.isEmpty()) {
            drawString(fontRendererObj, "§7Nearby Players:", 5, 5, 0xFFFFFF);
            int playerY = 20;
            for (MockDataManager.PlayerData player : nearbyPlayers) {
                String distance = String.format("%.1f", player.distance);
                drawString(fontRendererObj, 
                    String.format("§8> §f%s §7(%sm)", player.name, distance),
                    5, playerY, 0xFFFFFF);
                playerY += 12;
            }
        }
        
        // Draw recent alerts
        List<MockDataManager.ACAlert> alerts = MockDataManager.getRecentAlerts();
        if (!alerts.isEmpty()) {
            drawString(fontRendererObj, "§c§lRecent Alerts:", width - 155, 5, 0xFFFFFF);
            int alertY = 20;
            for (MockDataManager.ACAlert alert : alerts.subList(0, Math.min(3, alerts.size()))) {
                drawString(fontRendererObj, 
                    String.format("§8> §f%s §7(%s)", alert.player, alert.hack),
                    width - 155, alertY, 0xFFFFFF);
                alertY += 12;
            }
        }
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: // Anti-Cheat
                mc.displayGuiScreen(new AntiCheatGui(this));
                break;
            case 2: // Player Lookup
                mc.displayGuiScreen(new PlayerLookupGui(this));
                break;
            case 3: // Punishments
                mc.displayGuiScreen(new PunishmentGui(this));
                break;
            case 4: // Reports
                // TODO: Reports GUI
                break;
            case 5: // Vanish
                toggleVanish();
                break;
            case 6: // Staff Chat
                toggleStaffChat();
                break;
            case 7: // Settings
                mc.displayGuiScreen(new SettingsGui(this));
                break;
            case 8: // Disconnect
                mc.displayGuiScreen(new DisconnectConfirmGui(this));
                break;
        }
    }
    
    private void toggleVanish() {
        // TODO: Implement vanish toggle
    }
    
    private void toggleStaffChat() {
        // TODO: Implement staff chat toggle
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
} 