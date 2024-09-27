package xyz.nayskutzu.mythicalclient.listeners;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class which renders text
 */
public class RenderListener extends Gui {

    private int i = 0;
    private Color c = randomColor();
    private boolean regenerateText = true;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            if (KeyListener.startRendering()) {
                if (regenerateText) {
                    Random random = new Random();
                    i = random.nextInt(wittyComments.length - 1);
                    c = randomColor();
                    regenerateText = false;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            regenerateText = true;
                        }
                    }, 4000);
                }
                drawAlert(wittyComments[i], c);

            }
        }
    }

    /**
     * Method to draw the alert using a random color
     */
    private void drawAlert(String comment, Color c) {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        drawCenteredString(renderer, comment, resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, c.getRGB());
    }


    /**
     * An array with witty comments, for reproaching the player
     */
    private String[] wittyComments = new String[]{
            "Relax, nerd!",
            "You sure you need to use that word?",
            "Would be much better if you hadn't wrote this...",
            "Calm yourself, it's a block game!",
            "Wouldn't it be a better idea not to say anything?",
            "No u",
            "Is it really necessary to say that?",
            "You're not helping yourself with that...",
            "You're not helping anyone with that...",
            "You really want to get muted?",
            "You're not helping the community with that...",
            "You're not helping the server with that...",
            "Imagine being so mad you have to type that...",
            "Imagine getting muted for that...",
            "Imagine getting banned for that...",
            "Imagine getting kicked for that...",
            "Imagine i mute you :)",
            "Do not say that again or i will mute you :)"
    };


    /**
     * Generates a random color
     *
     * @return A random color
     */
    private Color randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r, g, b);
    }

}