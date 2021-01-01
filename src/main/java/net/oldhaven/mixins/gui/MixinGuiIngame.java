package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.ItemKeep;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.options.SavedLogins;
import net.oldhaven.customs.packets.all.CPacketMobHealth;
import net.oldhaven.customs.packets.util.Packets;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.OnScreenText;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {
    private String lastItem = "";
    private int timeUp = 0;
    private int itemFade = 0;
    @Shadow private Minecraft mc;
    @Shadow private static RenderItem itemRenderer;
    @Shadow private List<String> chatMessageList;

    private void drawHealth(int x, int y, boolean half) {
        if(!half)
            drawTexturedModalRect(x, y, 52, 0, 9, 9);
        else
            drawTexturedModalRect(x, y, 61, 0, 9, 9);
    }
    private void drawMissingHealth(int x, int y, int heartsLife) {
        boolean flag1 = (mc.thePlayer.heartsLife / 3) % 2 == 1;
        if(mc.thePlayer.heartsLife < 10)
            flag1 = false;
        int k5 = 0;
        if(flag1)
            k5 = 1;
        drawTexturedModalRect(x, y, 16 + k5 * 9, 0, 9, 9);
    }

    private DecimalFormat df = new DecimalFormat("#,###,##0.00");
    @Inject(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;showDebugInfo:Z", shift = At.Shift.BEFORE))
    private void renderGameOverlay_Text(CallbackInfo ci) {
        if(!this.mc.gameSettings.showDebugInfo) {
            if(mc.isGamePaused)
                return;
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            if(ModOptions.MODERN_TOOLTIPS.getAsBool()) {
                ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
                if (stack != null) {
                    String name = stack.getItem().getStatName();
                    if (!lastItem.equals(name)) {
                        lastItem = name;
                        itemFade = 255;
                        timeUp = 0;
                    }
                    if (itemFade > 0) {
                        this.drawCenteredString(mc.fontRenderer, name, k / 2, l - 54, adjustAlpha(itemFade));
                        if (timeUp > 50)
                            itemFade -= 3F;
                        else
                            timeUp++;
                    }
                } else if (!lastItem.equals(""))
                    lastItem = "";
            }
            if(ModOptions.SHOW_SPEED_IN_GAME.getAsBool()) {
                double speed = MMUtil.getPlayer().getPlayerSpeed();
                OnScreenText.replaceOnScreenText("speed", "Speed: " + df.format(speed*(0.98F * 5)), 0xffffff);
            } else
                OnScreenText.hideOnScreenText("speed");
            if(ModOptions.SHOW_MOTION_IN_GAME.getAsBool()) {
                double motion = MMUtil.getPlayer().getPlayerMotion();
                OnScreenText.replaceOnScreenText("motion", "Motion: " + df.format(motion*(0.98F * 5)), 0xffffff);
            } else
                OnScreenText.hideOnScreenText("motion");
            int down = 2;
            for(Map.Entry<String, OnScreenText> entry : OnScreenText.getOnScreenText().entrySet()) {
                if(!entry.getKey().isEmpty()) {
                    //mc.fontRenderer.renderString(entry.getValue().getText(), 2, down, new float[]{}, false);
                    this.drawString(mc.fontRenderer, entry.getValue().getText(), 2, down, adjustAlpha(entry.getValue().getColor(), 255));
                    down+=12;
                }
            }
            int right = 2;
            int up = l - 50 - (chatMessageList.size() * 9);
            if(chatMessageList.size() >= 21)
                up = l - 50 - (21 * 9);
            for(int i=0;i < MMUtil.playersTyping.size();i++) {
                String name = MMUtil.playersTyping.get(i);
                if(i+1 != MMUtil.playersTyping.size())
                    name += ",";
                mc.fontRenderer.drawStringWithShadow(name, right, up, 0xffffff);
                right += mc.fontRenderer.getStringWidth(name) + 5;
            }
            if(MMUtil.playersTyping.size() > 0) {
                this.drawString(mc.fontRenderer, "is typing...", right, up, 0xffffff);
            }
            //if(gs.getOptionI("Toggle XP-Bar") == 1)
                //guiEXPBar(scaledresolution);
            if(ModOptions.TOGGLE_WAILA.getAsBool())
                guiWAILA(scaledresolution);
            if(!ModOptions.DISABLE_PLAYERLIST.getAsBool() && MMUtil.playerList)
                guiPlayerList(scaledresolution);
        }
    }

    /*private int k, l, current, after;
    @Inject(method = "renderGameOverlay", at = @At(value = "HEAD", target = "Lnet/minecraft/src/GuiIngame;drawTexturedModalRect(IIIIII)V"))
    private void draw0(float v, boolean b, int i, int i1, CallbackInfo ci) {
        if(mc == null || mc.gameSettings == null)
            return;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        k = scaledresolution.getScaledWidth();
        l = scaledresolution.getScaledHeight();
        if(mc.thePlayer == null)
            return;
        InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        current = (k / 2 - 91 - 1) + inventoryPlayer.currentItem * 20 + 20;
        after = current-121;
        if(after > 180)
            after = 180;
    }

    @Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 0))
    private void draw1(GuiIngame guiIngame, int i, int i1, int i2, int i3, int i4, int i5) {
        if(current == 0)
            return;
        float f = ModOptions.RAISE_SELECTED_HOTBAR.getAsFloat();
        if(f > 0.0F) {
            drawTexturedModalRect(current, l - 22, after, 0, 301-(current)+2, 22);
            drawTexturedModalRect(k / 2 - 91, l - 22, 0, 0, current-141+2, 22);
        } else
            drawTexturedModalRect(i, i1, i2, i3, i4, i5);
    }

    @Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 1))
    private void draw2(GuiIngame guiIngame, int i, int i1, int i2, int i3, int i4, int i5) {
        if(current == 0)
            return;
        float f = ModOptions.RAISE_SELECTED_HOTBAR.getAsFloat()*5;
        if(f > 0.0F) {
            InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
            drawTexturedModalRect((k / 2 - 91 - 1) + inventoryPlayer.currentItem * 20, l - 22 - (int)f, 0, 22, 24, 22);
        } else
            drawTexturedModalRect(i, i1, i2, i3, i4, i5);
    }

    @Redirect(method = "renderInventorySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;renderItemIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/ItemStack;II)V", ordinal = 0))
    private void draw3(RenderItem renderItem, FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int j, int k) {
        if(current == 0)
            return;
        float f = ModOptions.RAISE_SELECTED_HOTBAR.getAsFloat()*5;
        if(f > 0.0F) {
            EntityPlayerSP player = mc.thePlayer;
            if(itemStack == player.getCurrentEquippedItem())
                renderItem.renderItemIntoGUI(fontRenderer, renderEngine, itemStack, j, k - (int)f);
            else
                renderItem.renderItemIntoGUI(fontRenderer, renderEngine, itemStack, j, k);
        } else
            renderItem.renderItemIntoGUI(fontRenderer, renderEngine, itemStack, j, k);
    }*/

    @Redirect(method = "renderInventorySlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderItem;renderItemOverlayIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/ItemStack;II)V", ordinal = 0))
    private void draw4(RenderItem renderItem, FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int j, int k) {
        float f = ModOptions.RAISE_SELECTED_HOTBAR.getAsFloat()*5;
        if(f > 0.0F) {
            EntityPlayerSP player = mc.thePlayer;
            if(itemStack == player.getCurrentEquippedItem())
                renderItem.renderItemOverlayIntoGUI(fontRenderer, renderEngine, itemStack, j, k - (int)f);
            else
                renderItem.renderItemOverlayIntoGUI(fontRenderer, renderEngine, itemStack, j, k);
        } else
            renderItem.renderItemOverlayIntoGUI(fontRenderer, renderEngine, itemStack, j, k);
    }

    private boolean getListFromChat = false;
    @Redirect(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/src/ChatLine;message:Ljava/lang/String;", opcode = 180))
    private String getMessage(ChatLine chatLine) {
        String msg = chatLine.message;
        if(getListFromChat) {
            if(msg.startsWith("§9There are §c2")) {
                String rep = msg.replace("§9There are §c", "");
                rep = rep.replaceAll("§9", "");
                playersOnline = Integer.parseInt(rep.split(" ")[0]);
                rep = rep.replace(playersOnline + " out of a maximum §c", "");
                maxPlayers = Integer.parseInt(rep.split(" ")[0]);
                chatMessageList.remove(chatLine);
            }
            for(int i=0;i < chatMessageList.size();i++) {

            }
            getListFromChat = false;
            return msg;
        }
        boolean b =
                msg.equals("§cPlease identify yourself with /login <password>") ||
                msg.equals("§cPlease login with \"/login password\"");
        if(!MMUtil.hasLoggedIn && b) {
            /* holy this was long */
            SavedLogins savedLoginsClass = MMUtil.getAutoLogins();
            SavedLogins.SavedLogin logins = savedLoginsClass.getSavedLoginsByIP(MMUtil.getPlayer().getConnectedServer());
            if(logins != null) {
                String login = logins.getName(mc.thePlayer.username);
                if(login != null) {
                    mc.thePlayer.sendChatMessage("/login " + login);
                    MMUtil.hasLoggedIn = true;
                } else
                    MMUtil.hasLoggedIn = true; /* ignore */
            } else
                MMUtil.hasLoggedIn = true; /* ignore */
        }
        return chatLine.message;
    }

    private int playersOnline = 0;
    private int maxPlayers = 100;
    private void guiPlayerList(ScaledResolution sc) {
        int width = sc.getScaledWidth();
        int centerW = sc.getScaledWidth()/2;
        if(Packets.canUsePackets()) {
            List<String> names = MMUtil.getPlayer().getJoinedNames();
            int height = sc.getScaledHeight()/2 - (12 * names.size());
            int graidentOH = height - 12;
            int gradientH = height + (12 * names.size()) + 12;
            drawGradientRect(width / 2 - 45, graidentOH,  width / 2 + 45, gradientH, 0xc0101010, 0xd0101010);
            this.drawCenteredString(mc.fontRenderer, "PLAYERLIST", centerW, graidentOH, 0x4fedff);
            for(String name : names) {
                this.drawCenteredString(mc.fontRenderer, name, centerW, height, 0xffffff);
                height += 12;
            }
            playersOnline = names.size();
            this.drawCenteredString(mc.fontRenderer, playersOnline + " players online", centerW, height, 0x4fedff);
        } else {
            this.drawCenteredString(mc.fontRenderer, playersOnline+"/"+maxPlayers, centerW, width/2, 0x4fedff);
        }
    }

    private void guiEXPBar(ScaledResolution sc) {
        int width = sc.getScaledWidth();
        int height = sc.getScaledHeight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int o = mc.renderEngine.getTexture("/gui/alphabg.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(o);

        int i = 10;
        int h = height-29;
        drawTexturedModalRect(width/2-(182/2), h, 0, 26, 1, 5);
        drawTexturedModalRect(width/2-(182/2)+1+i, h, 1, 26, 179-i, 5);
        drawTexturedModalRect(width/2-(182/2)+180, h, 180, 26, 1, 5);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void guiWAILA(ScaledResolution sc) {
        Entity entity = MMUtil.getPlayer().pointingEntity;
        int blockId = MMUtil.pointingBlock;
        int width = sc.getScaledWidth();

        if(entity instanceof EntityLiving || blockId != 0) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int o = mc.renderEngine.getTexture("/gui/alphabg.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(o);
            drawTexturedModalRect(width/2-45, 3, 0, 0, 87, 26);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(GL11.GL_BLEND);
        }

        if (entity instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving) entity;
            String name;
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityLiving;
                name = player.username;
            } else
                name = getEntityName(entityLiving);


            drawCenteredString(mc.fontRenderer, name, width / 2, 6, 0xffffff);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/icons.png"));
            int health = entityLiving.health;

            String connectedServer = MMUtil.getPlayer().getConnectedServer();
            if(connectedServer != null && CPacketMobHealth.mobIds.containsKey(entity.entityId))
                health = CPacketMobHealth.mobIds.get(entity.entityId);

            for (int i = 0; i < 10; i++) {
                int i6 = (width / 2 - 42) + i * 8;
                drawMissingHealth(i6, 15, entity.heartsLife);
            }
            for (int i = 0; i < health; i++) {
                int i6 = (width / 2 - 42) + i * 8;
                if (i * 2 + 1 < health) {
                    drawHealth(i6, 15, false);
                } else if (i * 2 + 1 == health) {
                    drawHealth(i6, 15, true);
                }
            }
        } else if(blockId != 0) {
            Block block = Block.blocksList[blockId];
            if(block != null) {
                drawCenteredString(mc.fontRenderer, getBlockName(block.translateBlockName()), width / 2 + 5, 6, 0xffffff);
                drawCenteredString(mc.fontRenderer, "H: " + block.getHardness(), width / 2 + 5, 16, 0xffffff);
                itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(block), width / 2 - 40, 8);
                ItemKeep.ItemFulfill fulfill = ItemKeep.getByStr(block.getBlockName());
                if(fulfill != null && fulfill.destroyWith != null) {
                    itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, fulfill.destroyWith.stack, width / 2 + 40, 8);
                }
                RenderHelper.disableStandardItemLighting();
            }
        }
    }

    private String getBlockName(String name) {
        if(name.startsWith("tile")) {
            name = name.replace("tile.", "");
            name = name.replace(".name", "");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }

    private String getEntityName(EntityLiving entityLiving) {
        String name = entityLiving.getEntityTexture();
        name = name.replace("/mob/", "");
        name = name.replace(".png", "");
        name = name.replace(".png", "");
        name = name.replace(".jpg", "");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public int adjustAlpha(int rgb, int alpha) {
        int red = (rgb>>16) &0xff;
        int green = (rgb>>8) &0xff;
        int blue = (rgb>>0) &0xff;
        return (alpha&0x0ff)<<24 | red<<16 | green<<8 | blue;
    }

    public int adjustAlpha(int alpha) {
        Color color = new Color(255, 255, 255);
        int rgb = color.getRGB();
        int red = (rgb>>16) &0xff;
        int green = (rgb>>8) &0xff;
        int blue = (rgb>>0) &0xff;
        return (alpha&0x0ff)<<24 | red<<16 | green<<8 | blue;
    }

    /* 37 '2' values*/
    @ModifyConstant(
            method = "renderGameOverlay",
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/src/GuiIngame;chatMessageList:Ljava/util/List;"), to = @At("RETURN")),
            constant = @Constant(intValue = 9, ordinal = 0))
    private int changeIntValue(int oV) {
        return (9)+(MMUtil.chatScrollUp*9);
    }
}
