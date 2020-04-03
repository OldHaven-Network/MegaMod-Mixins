package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.oldhaven.gui.CustomGuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen extends Gui {
    private CustomGuiButton.GuiTextField textFieldToType;

    @Inject(method = "keyTyped", at = @At("RETURN"))
    private void keyTyped(char c, int i, CallbackInfo ci) {
        if(textFieldToType != null) {
            textFieldToType.keyTyped(c, i);
        }
    }

    /*private int tick = 0;
    private String lastName = "";
    private int up = 24;
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiButton;drawButton(Lnet/minecraft/client/Minecraft;II)V"))
    private void redirectDrawScreen(GuiButton button, Minecraft minecraft, int i, int i1) {
        if(!lastName.equals(this.getClass().getSimpleName())) {
            this.lastName = this.getClass().getSimpleName();
            this.up = 48;
        }
        if(this.up > 0) {
            this.tick++;
            if (this.tick > 150) {
                this.tick = 0;
                this.up /= 1.05F;
            }
        } else if(this.up < 0)
            this.up = 0;
        button.drawButton(minecraft, i, i1);
    }*/

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton guiButton, CallbackInfo ci) {
        if(textFieldToType != null)
            textFieldToType.isFocused = false;
        if(guiButton instanceof CustomGuiButton.GuiTextField) {
            CustomGuiButton.GuiTextField textField = (CustomGuiButton.GuiTextField)guiButton;
            textField.isFocused = false;
            textFieldToType = textField;
        } else
            textFieldToType = null;
    }
}
