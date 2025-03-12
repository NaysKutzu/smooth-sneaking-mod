package xyz.nayskutzu.mythicalclient.gui.components;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import java.awt.Color;

public class ColorButton extends GuiButton {
    private final int baseColor;
    private float hoverTime = 0;
    
    public ColorButton(int buttonId, int x, int y, int width, int height, String text, int color) {
        super(buttonId, x, y, width, height, text);
        this.baseColor = color;
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && 
                            mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            
            // Smooth hover animation
            if (hovered && hoverTime < 1) {
                hoverTime += 0.1f;
            } else if (!hovered && hoverTime > 0) {
                hoverTime -= 0.1f;
            }
            hoverTime = Math.max(0, Math.min(1, hoverTime));
            
            // Calculate colors
            int color = new Color(baseColor).darker().getRGB();
            int hoverColor = baseColor;
            int finalColor = interpolateColor(color, hoverColor, hoverTime);
            
            // Draw button background
            drawRect(this.xPosition, this.yPosition,
                    this.xPosition + this.width, this.yPosition + this.height,
                    new Color(40, 40, 40, 200).getRGB());
            
            // Draw colored accent
            drawRect(this.xPosition, this.yPosition,
                    this.xPosition + this.width, this.yPosition + 2,
                    finalColor);
            
            // Draw text with shadow
            this.drawCenteredString(mc.fontRendererObj, this.displayString,
                this.xPosition + this.width / 2,
                this.yPosition + (this.height - 8) / 2,
                0xFFFFFF);
        }
    }
    
    private int interpolateColor(int color1, int color2, float factor) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int r = (int)(r1 + (r2 - r1) * factor);
        int g = (int)(g1 + (g2 - g1) * factor);
        int b = (int)(b1 + (b2 - b1) * factor);
        
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public void setColor(int i) {
        return;
    }
} 