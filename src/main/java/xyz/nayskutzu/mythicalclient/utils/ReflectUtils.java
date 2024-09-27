package xyz.nayskutzu.mythicalclient.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;

import java.lang.reflect.Field;

/**
 * A small class to help access protected/private values from the {@link net.minecraft.client.gui.GuiChat} class
 */
public class ReflectUtils {

    /**
     * A method which uses reflection to get the chat text field, since it has protected access
     *
     * @return Text field
     */
    public static GuiTextField getMainTextField() {
        try {
            // Declaration so we can use it in multiple try/catch blocks
            Field fieldText;
            try {
                // Try to use the obfuscated name for the input field
                fieldText = GuiChat.class.getDeclaredField("field_146415_a");
            } catch (NoSuchFieldException e) {
                // Obfuscated name wasn't found, so try the de-obfuscated name
                fieldText = GuiChat.class.getDeclaredField("inputField");
            }
            fieldText.setAccessible(true);
            return ((GuiTextField) fieldText.get(Minecraft.getMinecraft().currentScreen));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException ignored) {
            return null;
        }
        return null;
    }
}