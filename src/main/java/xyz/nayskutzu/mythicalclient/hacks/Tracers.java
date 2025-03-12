package xyz.nayskutzu.mythicalclient.hacks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;
import xyz.nayskutzu.mythicalclient.utils.FriendlyPlayers;

public class Tracers {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean enabled;
    private static final Tracers instance = new Tracers();
    private long startTime = System.currentTimeMillis();

    public static void main() {
        if (enabled) {
            enabled = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(instance);
            MythicalClientMod.sendMessageToChat("&7Tracers are now &cdisabled&7.",false);
        } else {
            enabled = true;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(instance);
            MythicalClientMod.sendMessageToChat("&7Tracers are now &aenabled&7.",false);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer || !player.isEntityAlive())
                continue;
            String playerName = player.getName();
            if (playerName != null && !playerName.isEmpty() && !playerName.matches(".*§.*")
                    && !playerName.matches(".*&.*") && !playerName.matches(".*CIT-.*")) {
                if (FriendlyPlayers.isFriendly(playerName)) {
                    continue;
                }
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                        - mc.getRenderManager().viewerPosX;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                        - mc.getRenderManager().viewerPosY;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                        - mc.getRenderManager().viewerPosZ;
                if (player == mc.thePlayer || mc.thePlayer.isSpectator()) {
                    return;
                }
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

        float hue = (System.currentTimeMillis() - startTime) % 3000 / 3000f;
        float[] rgb = getRainbowColor(hue);
        
        GL11.glLineWidth(2.0F);
        GlStateManager.color(rgb[0], rgb[1], rgb[2], 0.75F);

        GL11.glBegin(GL11.GL_LINES);
        GlStateManager.color(rgb[0], rgb[1], rgb[2], 0.1F);
        GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
        
        GlStateManager.color(rgb[0], rgb[1], rgb[2], 1.0F);
        GL11.glVertex3d(x, y + 1.62, z);
        GL11.glEnd();

        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private float[] getRainbowColor(float hue) {
        float[] rgb = new float[3];
        int rgb_i = java.awt.Color.HSBtoRGB(hue, 0.8f, 1.0f);
        rgb[0] = (rgb_i >> 16 & 255) / 255.0f;
        rgb[1] = (rgb_i >> 8 & 255) / 255.0f;
        rgb[2] = (rgb_i & 255) / 255.0f;
        return rgb;
    }
}
