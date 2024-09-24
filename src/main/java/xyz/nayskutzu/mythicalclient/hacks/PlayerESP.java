package xyz.nayskutzu.mythicalclient.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

        } else {
            enabled = true;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(instance);

        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null)
            return;

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer)
                continue;

            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
                    - mc.getRenderManager().viewerPosX;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
                    - mc.getRenderManager().viewerPosY;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks
                    - mc.getRenderManager().viewerPosZ;

            drawESP(x, y, z, player.width, player.height);
            drawTracer(x, y, z);
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

    /**
     * @deprecated This method is not used
     */
    private void drawTracer(double x, double y, double z) {
        // REMOVE THIS METHOD
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
