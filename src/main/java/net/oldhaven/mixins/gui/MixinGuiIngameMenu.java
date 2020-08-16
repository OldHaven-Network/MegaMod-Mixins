package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.gui.autologins.GuiAutoLogins;
import net.oldhaven.gui.itemkeep.GuiItemKeep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {
    private GuiButton cheats;
    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        int[] wh = { 98, 20 };
        if(MMUtil.getPlayer().getConnectedServer() != null)
            controlList.add(new GuiSmallButton(7, width - wh[0] - 5, 4, wh[0], wh[1], "AutoLogins"));
        else {
            controlList.add(cheats = new GuiSmallButton(9,width - wh[0] - 5, 4, wh[0], wh[1], "Enable "));
            cheats.displayString = ModOptions.SP_CHEATS.getAsBool() ? "Disable Cheats" : "Enable Cheats";
        }
        controlList.add(new GuiSmallButton(10,width - wh[0] - 5, 4+24, wh[0], wh[1], "Item IDs"));
        StringTranslate var2 = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(8, this.width / 2 - 100, this.height / 4 + 48 + 24 + -16, var2.translateKey("menu.mods")));
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton guibutton, CallbackInfo ci) {
        if(guibutton.id == 7)
            mc.displayGuiScreen(new GuiAutoLogins(this));
        if(guibutton.id == 8)
            mc.displayGuiScreen(new GuiTexturePacks(this));
        if(guibutton.id == 9) {
            MMUtil.getCustomGameSettings().setOptionBtn("SP Cheats");
            MMUtil.getCustomGameSettings().saveSettings();
            boolean b = ModOptions.SP_CHEATS.getAsBool();
            cheats.displayString = b ? "Disable Cheats" : "Enable Cheats";
            mc.thePlayer.addChatMessage(b ? "Cheats were enabled globally" : "Cheats were disabled globally");
        } else if(guibutton.id == 10) {
            mc.displayGuiScreen(new GuiItemKeep(this));
        }
    }
}
