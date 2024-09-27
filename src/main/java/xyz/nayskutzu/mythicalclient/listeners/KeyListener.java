package xyz.nayskutzu.mythicalclient.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.nayskutzu.mythicalclient.utils.ReflectUtils;
import xyz.nayskutzu.mythicalclient.utils.WordsList;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class which listens to stuff written in the chat text field
 */
public class KeyListener extends WordsList {

    private static boolean render = false;

    @SubscribeEvent
    public void onTick(TickEvent event) {
        // Checking if the chat gui is opened
        if (FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
            // Get the text field
            GuiTextField field = ReflectUtils.getMainTextField();
            if (check(field)) {
                Minecraft.getMinecraft().displayGuiScreen(null);
                render = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        render = false;
                    }
                }, 3500);
            }
        }

    }

    static boolean startRendering() {
        return render;
    }

    private boolean check(GuiTextField field) {
        return
        // Field is valid
        field != null &&
        // Player has written in text it
                !field.getText().isEmpty() &&
                // The current text contains any word from the words list
                getWords().stream().anyMatch(field.getText().toLowerCase()::contains);
    }

}