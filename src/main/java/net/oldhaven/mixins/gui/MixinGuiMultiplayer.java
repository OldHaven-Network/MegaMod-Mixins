package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.MegaMod;
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
    private MegaMod megaMod;
    @Shadow private GuiScreen parentScreen;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void GuiMultiplayer(GuiScreen guiScreen, CallbackInfo ci) {
        megaMod = MegaMod.getInstance();
    }

    /**
     * @author ashleez_
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        createButtons();
        String s = mc.gameSettings.lastServer.replaceAll("_", ":");
        slotGui = new GuiMultiplayerSlot(this);
        slotGui.registerScrollButtons(controlList, 7, 8);
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
     * @author ashleez_
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void actionPerformed(GuiButton guibutton) {
        if(!guibutton.enabled) {
            return;
        }
        if(guibutton.id == 0)
            mc.displayGuiScreen(parentScreen);
        String serverKey = null;
        Map<String, String> savedServers = megaMod.getSavedServers().getSavedServersMap();
        if(megaMod.getSavedServers().selectedServer != -1)
            serverKey = (String)savedServers.keySet().toArray()[megaMod.getSavedServers().selectedServer];
        if(serverKey != null) {
            if(guibutton.id == 1) {
                String ip = savedServers.get(serverKey);
                String[] as = ip.split(":");
                mc.displayGuiScreen(new GuiConnecting(mc, as[0], as.length <= 1 ? 25565 : parseIntWithDefault(as[1], 25565)));
                return;
            } else if(guibutton.id == 2) {
                megaMod.getSavedServers().selectedServer = -1;
                megaMod.getSavedServers().removeServer(serverKey);
                megaMod.getSavedServers().saveServers();
                return;
            } else if(guibutton.id == 7) {
                mc.displayGuiScreen(new GuiMultiplayerEditServer(this, new ServerInfo(serverKey, savedServers.get(serverKey))));
                return;
            }
        }
        if(guibutton.id == 3)
            mc.displayGuiScreen(new GuiMultiplayerEditServer(this));
        else if(guibutton.id == 4)
            mc.displayGuiScreen(new GuiMultiplayerDirectConnect(this));
        else if(guibutton.id == 7)
            mc.displayGuiScreen(new GuiMultiplayerEditServer(this, null));
        else
            slotGui.actionPerformed(guibutton);
    }

    /**
     * @author ashleez_
     * @reason New MP Gui, Crashes game otherwise
     * @param var1
     * @param var2
     */
    @Overwrite
    protected void keyTyped(char var1, int var2) {
        
    }

    /**
     * @author ashleez_
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void updateScreen()
    {
        //serverTextBox.updateCursorCounter();
    }

    /**
     * @author ashleez_
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void drawScreen(int i, int j, float f)
    {
        slotGui.drawScreen(i, j, f);
        if(MegaMod.getInstance().getSavedServers().selectedServer != -1)
            btnEditServer.enabled = btnDeleteServer.enabled = btnSelectServer.enabled = true;
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawCenteredString(fontRenderer, stringtranslate.translateKey("multiplayer.title"), width / 2, 16, 0xffffff);
        super.drawScreen(i, j, f);
    }

    /**
     * @author ashleez_
     * @reason New Multilayer GUI
     */
    @Overwrite
    public void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
    }

    private int parseIntWithDefault(String s, int i) {
        try {
            return Integer.parseInt(s.trim());
        } catch(Exception exception) {
            return i;
        }
    }
}
