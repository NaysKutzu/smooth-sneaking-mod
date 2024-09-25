package xyz.nayskutzu.mythicalclient.hacks;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;
import xyz.nayskutzu.mythicalclient.utils.ChatColor;

public class PlayerHealth {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean enabled;
    private static final PlayerHealth instance = new PlayerHealth();

    public static void main() {
        System.out.println("PlayerHealth class booting up...");
        if (enabled) {
            enabled = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(instance);
            MythicalClientMod.sendMessageToChat("&7Player health is now &cdisabled&7.",false);
        } else {
            enabled = true;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(instance);
            MythicalClientMod.sendMessageToChat("&7Player health is now &aenabled&7.",false);
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<?> event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity; // Accessing the entity directly

            // Avoid displaying health for self and NPCs with empty or color-coded names
            String playerName = player.getName();
            if (player != mc.thePlayer && playerName != null && !playerName.isEmpty() && !playerName.matches(".*§.*")
                    && !playerName.matches(".*&.*")) {
                //displayHealthInChat(player);
                displayHealthUnderName(player);
            }
        }
    }

    private void displayHealthUnderName(EntityPlayer player) {
        // Get player health
        float health = player.getHealth();
        //float maxHealth = player.getMaxHealth();

        // Prepare health text
        int hearts = (int) Math.ceil(health / 2.0);
        StringBuilder healthTextBuilder = new StringBuilder();
        for (int i = 0; i < hearts; i++) {
            healthTextBuilder.append(ChatColor.MAGIC + "❤");
        }
        String healthText = healthTextBuilder.toString();

        // Display health text under player name
        try {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) (player.posX - mc.getRenderManager().viewerPosX), (float) (player.posY - mc.getRenderManager().viewerPosY + player.height + 0.5F), (float) (player.posZ - mc.getRenderManager().viewerPosZ));
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-0.02F, -0.02F, 0.02F); // Adjusted scale to make the text bigger and flipped correctly
            GL11.glTranslatef(0.0F, -10.0F, 0.0F); // Move the text up
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            mc.fontRendererObj.drawString(healthText, -mc.fontRendererObj.getStringWidth(healthText) / 2, 0, 0xFFFFFF, true);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            mc.fontRendererObj.drawString(healthText, -mc.fontRendererObj.getStringWidth(healthText) / 2, 0, 0xFFFFFF);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            displayHealthInChat(player);
        } catch (Exception e) {
            e.printStackTrace();
            MythicalClientMod.sendMessageToChat(
                    "&cAn error occurred while rendering health under player name." + e.getMessage(),false);
        }
    }

    private void displayHealthInChat(EntityPlayer player) {
        return;
        // Get player health
        //float health = player.getHealth();
        //float maxHealth = player.getMaxHealth();

        // Prepare health text
        //String healthText = String.format("%s has %.1f / %.1f health", player.getName(), health, maxHealth);

        //MythicalClientMod.sendMessageToChat(healthText);
    }
}
