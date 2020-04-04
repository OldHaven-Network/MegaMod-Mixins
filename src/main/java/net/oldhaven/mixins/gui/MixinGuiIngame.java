package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.customs.SavedLogins;
import net.oldhaven.customs.packets.CustomPackets;
import org.checkerframework.common.util.report.qual.ReportReadWrite;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.objectweb.asm.Opcodes;
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

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        itemRenderer = new RenderItem();
    }

    private void drawHealth(int x, int y, boolean half) {
        if(!half) {
            drawTexturedModalRect(x, y, 52, 0, 9, 9);
        } else {
            drawTexturedModalRect(x, y, 61, 0, 9, 9);
        }
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
            MegaMod megaMod = MegaMod.getInstance();
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
            Integer tooltip = gs.getOptionI("Show Tooltip");
            if(tooltip != null && tooltip == 1) {
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
            int showSpeed = gs.getOptionI("Show Speed In-Game");
            int showMotion = gs.getOptionI("Show Motion In-Game");
            if(showSpeed == 1) {
                double speed = MegaMod.getPlayerInstance().getPlayerSpeed();
                MegaMod.getInstance().replaceOnScreenText("speed", "Speed: " + df.format(speed*(0.98F * 5)), 0xffffff);
            } else
                MegaMod.getInstance().hideOnScreenText("speed");
            if(showMotion == 1) {
                double motion = MegaMod.getPlayerInstance().getPlayerMotion();
                MegaMod.getInstance().replaceOnScreenText("motion", "Motion: " + df.format(motion*(0.98F * 5)), 0xffffff);
            } else
                MegaMod.getInstance().hideOnScreenText("motion");
            int down = 2;
            for(Map.Entry<String, MegaMod.OnScreenText> entry : MegaMod.getInstance().getOnScreenText().entrySet()) {
                if(!entry.getKey().isEmpty()) {
                    //mc.fontRenderer.renderString(entry.getValue().getText(), 2, down, new float[]{}, false);
                    this.drawString(mc.fontRenderer, entry.getValue().getText(), 2, down, adjustAlpha(entry.getValue().getColor(), 255));
                    down+=12;
                }
            }
            if(gs.getOptionI("Toggle WAILA") == 1) {
                guiWAILA(scaledresolution);
            }
            if(gs.getOptionI("Disable PlayerList") != 1 && MegaMod.getInstance().playerList) {
                guiPlayerList(scaledresolution);
            }
        }
    }

    @Redirect(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/src/ChatLine;message:Ljava/lang/String;", opcode = 180))
    private String getMessage(ChatLine chatLine) {
        MegaMod megaMod = MegaMod.getInstance();
        boolean b =
                chatLine.message.equals("§cPlease identify yourself with /login <password>") ||
                chatLine.message.equals("§cPlease login with \"/login password\"");
        if(!megaMod.hasLoggedIn && b) {
            /* holy this was long */
            SavedLogins savedLoginsClass = megaMod.getAutoLogins();
            SavedLogins.SavedLogin logins = savedLoginsClass.getSavedLoginsByIP(megaMod.getConnectedServer());
            if(logins != null) {
                String login = logins.getName(mc.thePlayer.username);
                if(login != null) {
                    mc.thePlayer.sendChatMessage("/login " + login);
                    megaMod.hasLoggedIn = true;
                } else
                    megaMod.hasLoggedIn = true; /* ignore */
            } else
                megaMod.hasLoggedIn = true; /* ignore */
        }
        return chatLine.message;
    }

    private void guiPlayerList(ScaledResolution sc) {
        int width = sc.getScaledWidth();
        int centerW = sc.getScaledWidth()/2;
        if(CustomPackets.canUsePackets()) {
            List<String> names = MegaMod.getInstance().getJoinedNames();
            int height = sc.getScaledHeight()/2 - (12 * names.size());
            int graidentOH = height - 12;
            int gradientH = height + (12 * names.size()) + 12;
            drawGradientRect(width / 2 - 45, graidentOH,  width / 2 + 45, gradientH, 0xc0101010, 0xd0101010);
            this.drawCenteredString(mc.fontRenderer, "PLAYERLIST", centerW, graidentOH, 0x4fedff);
            for(String name : names) {
                this.drawCenteredString(mc.fontRenderer, name, centerW, height, 0xffffff);
                height += 12;
            }
        } /*else {
            // later
        }*/
    }

    private void guiWAILA(ScaledResolution sc) {
        Entity entity = MegaMod.getInstance().pointingEntity;
        int blockId = MegaMod.getInstance().pointingBlock;
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
            drawCenteredString(mc.fontRenderer, getBlockName(block.translateBlockName()), width / 2 + 5, 6, 0xffffff);
            drawCenteredString(mc.fontRenderer, "H: " + block.getHardness(), width / 2 + 5, 16, 0xffffff);
            itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(block), width/2-40, 8);
            RenderHelper.disableStandardItemLighting();
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
        return (9)+(MegaMod.getInstance().chatScrollUp*9);
    }
}
