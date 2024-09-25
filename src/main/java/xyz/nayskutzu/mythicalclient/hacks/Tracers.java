package xyz.nayskutzu.mythicalclient.hacks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;

public class Tracers {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean enabled;
    private static final Tracers instance = new Tracers();

    public static void main() {
        if (enabled) {
            enabled = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(instance);
            MythicalClientMod.sendMessageToChat("&7Tracers are now &cdisabled&7.");
        } else {
            enabled = true;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(instance);
            MythicalClientMod.sendMessageToChat("&7Tracers are now &aenabled&7.");

        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer)
                continue;
            String playerName = player.getName();
            if (player != mc.thePlayer && playerName != null && !playerName.isEmpty() && !playerName.matches(".*§.*")
                    && !playerName.matches(".*&.*")) {
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                        - mc.getRenderManager().viewerPosX;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                        - mc.getRenderManager().viewerPosY;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                        - mc.getRenderManager().viewerPosZ;

                drawTracer(x, y, z);
            }

        }
    }

    private void drawTracer(double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(0.0F, 1.0F, 0.0F, 0.5F);
        GL11.glLineWidth(3.0F);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0); // Start at the player's eye height
        GL11.glVertex3d(x, y + mc.thePlayer.getEyeHeight(), z); // End at the other player's position
        GL11.glEnd();

        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
