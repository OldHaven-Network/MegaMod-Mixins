package net.oldhaven.mixins.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.oldhaven.gui.modsettings.GuiModSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiOptions.class)
public class MixinGuiOptions extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void InitReturn(CallbackInfo ci) {
        boolean guiApiExists = false;
        for(GuiButton guiButton : (List<GuiButton>)this.controlList) {
            if(guiButton.id != 300)
                continue;
            guiApiExists = true;
            break;
        }
        if(guiApiExists) {
            this.controlList.add(new GuiButton(172, this.width / 2 - 100, this.height / 6 + 96 - 24, "Mod Options..."));
        } else {
            this.controlList.add(new GuiButton(172, this.width / 2 - 100, this.height / 6 + 96 - 24 + 12, "Mod Options..."));
        }
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton btn, CallbackInfo ci) {
        if(btn.id == 172) {
            this.mc.displayGuiScreen(new GuiModSettings(this, this.mc.gameSettings));
        }
    }
}
