package xyz.nayskutzu.mythicalclient.mixin;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        try {
            Field inputField = GuiChat.class.getDeclaredField("inputField");
            inputField.setAccessible(true);
            Object inputFieldInstance = inputField.get(GuiChat.class.cast(this));
            if (inputFieldInstance == null) {
                System.out.println("inputFieldInstance is null in drawScreen.");
                return;
            }
            Field yPositionField = inputFieldInstance.getClass().getDeclaredField("yPosition");
            yPositionField.setAccessible(true);
            int yPosition = yPositionField.getInt(inputFieldInstance);
            yPositionField.setInt(inputFieldInstance, yPosition - 20); // Move the chat box up by 20 pixels
            System.out.println("Successfully moved chat box up by 20 pixels in drawScreen.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "initGui", at = @At("HEAD"))
    public void onInitGui(CallbackInfo ci) {
        try {
            Field inputField = GuiChat.class.getDeclaredField("inputField");
            inputField.setAccessible(true);
            Object inputFieldInstance = inputField.get(GuiChat.class.cast(this));
            if (inputFieldInstance == null) {
                System.out.println("inputFieldInstance is null in initGui.");
                return;
            }
            Field yPositionField = inputFieldInstance.getClass().getDeclaredField("yPosition");
            yPositionField.setAccessible(true);
            int yPosition = yPositionField.getInt(inputFieldInstance);
            yPositionField.setInt(inputFieldInstance, yPosition - 20); // Move the chat input box up by 20 pixels
            System.out.println("Successfully moved chat input box up by 20 pixels in initGui.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
