package xyz.nayskutzu.mythicalclient.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;

import org.lwjgl.opengl.GL11;

public class PlayerESP {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean enabled;
    private static final PlayerESP instance = new PlayerESP();

    public static void main() {
        System.out.println("PlayerESP class booting up...");
        if (enabled) {
            enabled = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(instance);
            MythicalClientMod.sendMessageToChat("&7Player ESP is now &cdisabled&7.",false);
        } else {
            enabled = true;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(instance);
            MythicalClientMod.sendMessageToChat("&7Player ESP is now &aenabled&7.",false);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer || player.isDead)
                continue;
            String playerName = player.getName();
            if (playerName != null && !playerName.isEmpty() && !playerName.matches(".*§.*")
                    && !playerName.matches(".*&.*")) {
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                        - mc.getRenderManager().viewerPosX;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                        - mc.getRenderManager().viewerPosY;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                        - mc.getRenderManager().viewerPosZ;
                if (player.isSneaking()) {
                    y -= 0.2;
                }
                if (player.isSprinting()) {
                    y += 0.2;
                }
                
                if (player == mc.thePlayer || mc.thePlayer.isSpectator()) {
                    return;
                }

                drawESP(x, y, z, player.width, player.height);
            }
        }
    }

    private void drawESP(double x, double y, double z, float width, float height) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GL11.glLineWidth(4.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);

        AxisAlignedBB box = new AxisAlignedBB(-width / 2, 0, -width / 2, width / 2, height, width / 2);
        drawBoundingBox(box);

        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawBoundingBox(AxisAlignedBB box) {
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);

        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);

        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
    }
}
