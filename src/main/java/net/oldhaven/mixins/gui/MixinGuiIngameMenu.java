package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.gui.autologins.GuiAutoLogins;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {
    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        if(MegaMod.getInstance().getConnectedServer() != null) {
            controlList.add(new GuiSmallButton(7, width - 154, 4, "AutoLogin Settings"));
        }
        StringTranslate var2 = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(8, this.width / 2 - 100, this.height / 4 + 48 + 24 + -16, var2.translateKey("menu.mods")));
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton guibutton, CallbackInfo ci) {
        if(guibutton.id == 7) {
            mc.displayGuiScreen(new GuiAutoLogins(this));
        }
        if(guibutton.id == 8) {
            mc.displayGuiScreen(new GuiTexturePacks(this));
        }
    }
}
