package net.oldhaven.gui.onscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.ItemKeep;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.packets.all.CPacketMobHealth;
import net.oldhaven.customs.util.MMUtil;
import org.lwjgl.opengl.GL11;

public class WailaUI extends OnScreenUI {
    public void draw(ScaledResolution sc, RenderItem itemRender) {
        Minecraft mc = getMinecraft();
        Entity entity = MMUtil.getPlayer().pointingEntity;
        int blockId = MMUtil.pointingBlock;

        boolean isEntity = entity instanceof EntityLiving;
        boolean isBlock = blockId != 0;

        int maxWidth = sc.getScaledWidth();
        int maxHeight = sc.getScaledHeight();
        int x = (int) (ModOptions.WAILA_POSITION_X.getAsFloat() * maxWidth);
        int y = (int) (ModOptions.WAILA_POSITION_Y.getAsFloat() * maxHeight);

        if(isEntity || isBlock) {
            int bgType = ModOptions.WAILA_BACKGROUND.getAsInt();
            if(bgType == 0)
                drawBackgroundImage(mc, x, y);
            else if(bgType == 1)
                drawBackgroundColored(mc, x, y);
        }

        if(isEntity)
            drawEntity(mc, entity, x, y);
        else if(isBlock)
            drawBlock(mc, itemRender, blockId, x, y);
    }

    private void drawBlock(Minecraft mc, RenderItem itemRender, int blockId, int x, int y) {
        Block block = Block.blocksList[blockId];
        if(block != null) {
            drawCenteredString(mc.fontRenderer, getBlockName(block.translateBlockName()), x + 5, y+3, 0xffffff);
            drawCenteredString(mc.fontRenderer, "H: " + block.getHardness(), x + 5, y+13, 0xffffff);
            itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(block), x - 40, y+5);
            ItemKeep.ItemFulfill fulfill = ItemKeep.getByStr(block.getBlockName());
            if(fulfill != null && fulfill.destroyWith != null) {
                itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, fulfill.destroyWith.stack, x + 40, y+5);
            }
            RenderHelper.disableStandardItemLighting();
        }
    }

    private void drawEntity(Minecraft mc, Entity entity, int x, int y) {
        EntityLiving entityLiving = (EntityLiving) entity;
        String name;
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            name = player.username;
        } else
            name = getEntityName(entityLiving);


        drawCenteredString(mc.fontRenderer, name, x, y+3, 0xffffff);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/icons.png"));
        int health = entityLiving.health;

        String connectedServer = MMUtil.getPlayer().getConnectedServer();
        if(connectedServer != null && CPacketMobHealth.mobIds.containsKey(entity.entityId))
            health = CPacketMobHealth.mobIds.get(entity.entityId);

        for (int i = 0; i < 10; i++) {
            int i6 = (x - 42) + i * 8;
            drawMissingHealth(i6, y+15, entity.heartsLife);
        }
        for (int i = 0; i < health; i++) {
            int i6 = (x - 42) + i * 8;
            if (i * 2 + 1 < health) {
                drawHealth(i6, y+15, false);
            } else if (i * 2 + 1 == health) {
                drawHealth(i6, y+15, true);
            }
        }
    }

    private void drawHealth(int x, int y, boolean half) {
        if(!half)
            drawTexturedModalRect(x, y, 52, 0, 9, 9);
        else
            drawTexturedModalRect(x, y, 61, 0, 9, 9);
    }
    private void drawMissingHealth(int x, int y, int hearts) {
        boolean flag1 = (hearts / 3) % 2 == 1;
        if(hearts < 10)
            flag1 = false;
        int k5 = 0;
        if(flag1)
            k5 = 1;
        drawTexturedModalRect(x, y, 16 + k5 * 9, 0, 9, 9);
    }

    public void drawBackgroundImage(Minecraft mc, int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int o = mc.renderEngine.getTexture("/gui/alphabg.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(o);
        drawTexturedModalRect(x-45, y, 0, 0, 87, 26);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
    }
    public void drawBackgroundColored(Minecraft mc, int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int width = 87/2;
        int height = 26;
        drawRect(x-45-1, y-1, x+width+1, y+height+1, 0xffffffff);
        drawRect(x-45-2, y, x+width+2, y+height, 0xffffffff);

        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public String getBlockName(String name) {
        if(name.startsWith("tile")) {
            name = name.replace("tile.", "");
            name = name.replace(".name", "");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }

    public String getEntityName(EntityLiving entityLiving) {
        String name = entityLiving.getEntityTexture();
        name = name.replace("/mob/", "");
        name = name.replace(".png", "");
        name = name.replace(".png", "");
        name = name.replace(".jpg", "");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}
