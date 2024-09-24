package xyz.nayskutzu.mythicalclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @Inject(method = "drawChat", at = @At("HEAD"))
    private void onDrawChat(int updateCounter, CallbackInfo info) {
        GlStateManager.translate(0.0F, -300.0F, 0.0F); // Move the chat up by 10 pixels
    }
}
