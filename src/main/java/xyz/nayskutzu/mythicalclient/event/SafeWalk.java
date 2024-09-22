package xyz.nayskutzu.mythicalclient.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;
import xyz.nayskutzu.mythicalclient.utils.Delay;
import xyz.nayskutzu.mythicalclient.utils.MythicalPlayer;

public class SafeWalk {
    public static SafeWalk instance = new SafeWalk();
    private Minecraft mc = Minecraft.getMinecraft();
    private MythicalClientMod safewalk = MythicalClientMod.instance;
    private MythicalPlayer player = MythicalPlayer.instance;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (MythicalClientMod.KeyBindSafewalk.isPressed()) { // func_151470_d() -> isPressed()
            this.safewalk.toggle();
            this.safewalk.sendToggle("Safewalk", "on", "off", this.safewalk.isToggled());
            if (!this.safewalk.isToggled()) {
                KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false); // func_74510_a() ->
                                                                                                   // setKeyBindState(),
                                                                                                   // field_74311_E ->
                                                                                                   // keyBindSneak,
                                                                                                   // func_151463_i() ->
                                                                                                   // getKeyCode()
            }
        }
        this.player.setUserSneaking(Keyboard.isKeyDown(this.mc.gameSettings.keyBindSneak.getKeyCode())); // func_151463_i()
                                                                                                         // ->
                                                                                                         // getKeyCode()
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player == this.mc.thePlayer && this.safewalk.isToggled()) { // field_71439_g -> thePlayer
            this.player.onTick();
        }
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        if (event.entity == this.mc.thePlayer) { // field_71439_g -> thePlayer
            this.player.onJump();
        }
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (MythicalClientMod.ToggleSneak) {
            new Delay(2000) {

                @Override
                public void onTick() {
                    MythicalClientMod.sendMessageToChat("Toggled sneak is installed please uninstall to use safewalk!");
                }
            };
        }
    }

}
