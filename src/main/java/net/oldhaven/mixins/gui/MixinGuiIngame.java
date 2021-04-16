package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.OnlinePlayer;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.options.SavedLogins;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.OnScreenText;
import net.oldhaven.gui.onscreen.MiniMapUI;
import net.oldhaven.gui.onscreen.OnScreenUI;
import net.oldhaven.gui.onscreen.PlayerListUI;
import net.oldhaven.gui.onscreen.WailaUI;
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
    @Shadow private List<ChatLine> chatMessageList;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Minecraft minecraft, CallbackInfo ci) {
        OnScreenUI.getUI(MiniMapUI.class).renderItem = itemRenderer;
    }

    private final DecimalFormat df = new DecimalFormat("#,###,##0.00");
    @Inject(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;showDebugInfo:Z", shift = At.Shift.BEFORE))
    private void renderGameOverlay_Text(CallbackInfo ci) {
        if(this.mc.gameSettings.showDebugInfo)
            return;
        if(mc.isGamePaused)
            return;
        if(mc.currentScreen != null)
            return;
        ScaledResolution sc = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = sc.getScaledWidth();
        int l = sc.getScaledHeight();
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
        String fps = this.mc.debug.split(" ")[0];
        OnScreenText.showIf(ModOptions.SHOW_FPS_IN_GAME.getAsBool(),
                "fps", "FPS: " + fps, 0xffffff);
        Vec3D pos = mc.thePlayer.getPosition(1.0F);
        String coords = (int) pos.xCoord + " " + (int) pos.yCoord + " " + (int) pos.zCoord;
        OnScreenText.showIf(ModOptions.SHOW_COORDS_IN_GAME.getAsBool(),
                "coords", "Coords: " + coords, 0xffffff);
        OnScreenText.showIf(ModOptions.SHOW_SPEED_IN_GAME.getAsBool(),
                "speed",
                "Speed: " + df.format(
                        MMUtil.getPlayer().getPlayerSpeed() *
                        (0.98F * 5)
                ), 0xffffff);
        OnScreenText.showIf(ModOptions.SHOW_MOTION_IN_GAME.getAsBool(),
                "motion", "Motion: " + df.format(
                        MMUtil.getPlayer().getPlayerMotion() *
                        (0.98F * 5)
                ), 0xffffff);
        int down = 2;
        for(Map.Entry<String, OnScreenText> entry : OnScreenText.getOnScreenText().entrySet()) {
            if(!entry.getKey().isEmpty()) {
                //mc.fontRenderer.renderString(entry.getValue().getText(), 2, down, new float[]{}, false);
                this.drawString(mc.fontRenderer, entry.getValue().getText(), 2, down, adjustAlpha(entry.getValue().getColor(), 255));
                down+=12;
            }
        }


        int visibleStrings = 0;
        byte var26 = 10;
        boolean var31 = false;
        if (this.mc.currentScreen instanceof GuiChat) {
            var26 = 20;
            var31 = true;
        }
        for(int var17 = 0; var17 < this.chatMessageList.size() && var17 < var26; ++var17) {
            if (((ChatLine) this.chatMessageList.get(var17)).updateCounter < 200 || var31) {
                double var32 = (double) ((ChatLine) this.chatMessageList.get(var17)).updateCounter / 200.0D;
                var32 = 1.0D - var32;
                var32 *= 10.0D;
                if (var32 < 0.0D)
                    var32 = 0.0D;
                if (var32 > 1.0D)
                    var32 = 1.0D;
                var32 *= var32;
                int var20 = (int) (255.0D * var32);
                if (var31)
                    var20 = 255;

                if (var20 > 0) {
                    visibleStrings++;
                }
            }
        }

        int right = 2;
        int up = l - 50 - (visibleStrings * 9);
        List<OnlinePlayer> typing = OnlinePlayer.getObjectiveList("typing");
        int size = typing.size();
        for(int i=0;i < typing.size();i++) {
            OnlinePlayer onlinePlayer = typing.get(i);
            if(!onlinePlayer.isTyping()) {
                size -= 1;
                continue;
            }
            String name = onlinePlayer.getUsername();
            if(i+1 != typing.size())
                name += ",";
            mc.fontRenderer.drawStringWithShadow(name, right, up, 0xffffff);
            right += mc.fontRenderer.getStringWidth(name) + 5;
        }
        if(size > 0)
            this.drawString(mc.fontRenderer, "is typing...", right, up, 0xffffff);

        //if(gs.getOptionI("Toggle XP-Bar") == 1)
            //guiEXPBar(scaledresolution);
        OnScreenUI.getUI(MiniMapUI.class).inGameTick(mc, (list) -> {
            OnScreenUI.getUI(MiniMapUI.class).prepareDrawBlock(list);
        });
        OnScreenUI.getUI(MiniMapUI.class).draw(sc, itemRenderer);
        if(ModOptions.WAILA_ENABLED.getAsBool())
            OnScreenUI.getUI(WailaUI.class).draw(sc, itemRenderer);
        if(!ModOptions.DISABLE_PLAYERLIST.getAsBool() && MMUtil.playerList)
            OnScreenUI.getUI(PlayerListUI.class).draw(sc);
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
                PlayerListUI.playersOnline = Integer.parseInt(rep.split(" ")[0]);
                rep = rep.replace(PlayerListUI.playersOnline + " out of a maximum §c", "");
                PlayerListUI.maxPlayers = Integer.parseInt(rep.split(" ")[0]);
                chatMessageList.remove(chatLine);
            }
            //for(int i=0;i < chatMessageList.size();i++) {

            //}
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
