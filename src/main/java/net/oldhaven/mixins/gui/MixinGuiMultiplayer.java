package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.gui.multiplayer.GuiMultiplayerDirectConnect;
import net.oldhaven.gui.multiplayer.GuiMultiplayerEditServer;
import net.oldhaven.gui.multiplayer.GuiMultiplayerSlot;
import net.oldhaven.gui.multiplayer.ServerInfo;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {
    private GuiButton btnEditServer;
    private GuiButton btnDeleteServer;
    private GuiButton btnSelectServer;
    private GuiButton btnRefresh;
    private GuiMultiplayerSlot slotGui;
    @Shadow private GuiScreen parentScreen;

    @Shadow private GuiTextField field_22111_h;
    private boolean noNew = false;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void GuiMultiplayer(GuiScreen guiScreen, CallbackInfo ci) {
        if(!ModOptions.NEW_MULTIPLAYER_GUI.getAsBool())
            this.noNew = true;
    }

    /**
     * @author cutezyash
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void initGui() {
        if(!noNew) {
            Keyboard.enableRepeatEvents(true);
            controlList.clear();
            createButtons();
            slotGui = new GuiMultiplayerSlot(this);
            slotGui.registerScrollButtons(controlList, 7, 8);
        } else {
            StringTranslate var1 = StringTranslate.getInstance();
            Keyboard.enableRepeatEvents(true);
            this.controlList.clear();
            this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("multiplayer.connect")));
            this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
            String var2 = this.mc.gameSettings.lastServer.replaceAll("_", ":");
            ((GuiButton)this.controlList.get(0)).enabled = var2.length() > 0;
            this.field_22111_h = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 4 - 10 + 50 + 18, 200, 20, var2);
            this.field_22111_h.isFocused = true;
            this.field_22111_h.setMaxStringLength(128);
        }
    }

    private void createButtons() {
        this.controlList.add(this.btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 22, 70, 20, "Edit"));
        this.controlList.add(this.btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 22, 70, 20, "Delete"));
        this.controlList.add(this.btnSelectServer = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Join Server"));
        this.controlList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Connect"));
        this.controlList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add Server"));
        this.controlList.add(this.btnRefresh=new GuiButton(8, this.width / 2 + 4, this.height - 22, 70, 20, "Refresh"));
        this.controlList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 22, 75, 20, "Cancel"));
        btnEditServer.enabled = btnDeleteServer.enabled = btnSelectServer.enabled = btnRefresh.enabled = false;
    }

    /**
     * @author cutezyash
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void actionPerformed(GuiButton guibutton) {
        if(!noNew) {
            if (!guibutton.enabled)
                return;
            if (guibutton.id == 0)
                mc.displayGuiScreen(parentScreen);
            String serverKey = null;
            Map<String, String> savedServers = MMUtil.getSavedServers().getSavedServersMap();
            if (MMUtil.getSavedServers().selectedServer != -1)
                serverKey = (String) savedServers.keySet().toArray()[MMUtil.getSavedServers().selectedServer];
            if (serverKey != null) {
                if (guibutton.id == 1) {
                    String ip = savedServers.get(serverKey);
                    String[] as = ip.split(":");
                    mc.displayGuiScreen(new GuiConnecting(mc, as[0], as.length <= 1 ? 25565 : parseIntWithDefault(as[1], 25565)));
                    return;
                } else if (guibutton.id == 2) {
                    MMUtil.getSavedServers().selectedServer = -1;
                    MMUtil.getSavedServers().removeServer(serverKey);
                    MMUtil.getSavedServers().saveServers();
                    return;
                } else if (guibutton.id == 7) {
                    mc.displayGuiScreen(new GuiMultiplayerEditServer(this, new ServerInfo(serverKey, savedServers.get(serverKey))));
                    return;
                }
            }
            if (guibutton.id == 3)
                mc.displayGuiScreen(new GuiMultiplayerEditServer(this));
            else if (guibutton.id == 4)
                mc.displayGuiScreen(new GuiMultiplayerDirectConnect(this));
            else if (guibutton.id == 7)
                mc.displayGuiScreen(new GuiMultiplayerEditServer(this, null));
            else
                slotGui.actionPerformed(guibutton);
        } else {
            if (guibutton.enabled) {
                if (guibutton.id == 1) {
                    this.mc.displayGuiScreen(this.parentScreen);
                } else if (guibutton.id == 0) {
                    String var2 = this.field_22111_h.getText().trim();
                    this.mc.gameSettings.lastServer = var2.replaceAll(":", "_");
                    this.mc.gameSettings.saveOptions();
                    String[] var3 = var2.split(":");
                    if (var2.startsWith("[")) {
                        int var4 = var2.indexOf("]");
                        if (var4 > 0) {
                            String var5 = var2.substring(1, var4);
                            String var6 = var2.substring(var4 + 1).trim();
                            if (var6.startsWith(":") && var6.length() > 0) {
                                var6 = var6.substring(1);
                                var3 = new String[]{var5, var6};
                            } else {
                                var3 = new String[]{var5};
                            }
                        }
                    }

                    if (var3.length > 2) {
                        var3 = new String[]{var2};
                    }

                    this.mc.displayGuiScreen(new GuiConnecting(this.mc, var3[0], var3.length > 1 ? this.parseIntWithDefault(var3[1], 25565) : 25565));
                }

            }
        }
    }

    /**
     * @author cutezyash
     * @reason New MP Gui, Crashes game otherwise
     * @param var1
     * @param var2
     */
    @Overwrite
    public void keyTyped(char var1, int var2) {
        if(noNew) {
            this.field_22111_h.textboxKeyTyped(var1, var2);
            if (var1 == '\r') {
                this.actionPerformed((GuiButton)this.controlList.get(0));
            }

            ((GuiButton)this.controlList.get(0)).enabled = this.field_22111_h.getText().length() > 0;
        }
    }

    /**
     * @author cutezyash
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void updateScreen()
    {
        if(noNew) {
            this.field_22111_h.updateCursorCounter();
        }
    }

    /**
     * @author cutezyash
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void drawScreen(int i, int j, float f)
    {
        if(!noNew) {
            slotGui.drawScreen(i, j, f);
            if (MMUtil.getSavedServers().selectedServer != -1)
                btnEditServer.enabled = btnDeleteServer.enabled = btnSelectServer.enabled = true;
            StringTranslate stringtranslate = StringTranslate.getInstance();
            drawCenteredString(fontRenderer, stringtranslate.translateKey("multiplayer.title"), width / 2, 16, 0xffffff);
        } else {
            StringTranslate var4 = StringTranslate.getInstance();
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
            this.drawString(this.fontRenderer, var4.translateKey("multiplayer.info1"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
            this.drawString(this.fontRenderer, var4.translateKey("multiplayer.info2"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 9, 10526880);
            this.drawString(this.fontRenderer, var4.translateKey("multiplayer.ipinfo"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
            this.field_22111_h.drawTextBox();
        }
        super.drawScreen(i, j, f);
    }

    /**
     * @author cutezyash
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        if(noNew) {
            this.field_22111_h.mouseClicked(i, j, k);
        }
    }

    private int parseIntWithDefault(String s, int i) {
        try {
            return Integer.parseInt(s.trim());
        } catch(Exception exception) {
            return i;
        }
    }
}
