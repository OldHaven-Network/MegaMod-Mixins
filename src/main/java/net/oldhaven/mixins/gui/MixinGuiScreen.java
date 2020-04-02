package net.oldhaven.mixins.gui;

import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.oldhaven.gui.CustomGuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton guiButton, CallbackInfo ci) {
        System.out.println("action?");
        if(textFieldToType != null)
            textFieldToType.isFocused = false;
        if(guiButton instanceof CustomGuiButton.GuiTextField) {
            CustomGuiButton.GuiTextField textField = (CustomGuiButton.GuiTextField)guiButton;
            textField.isFocused = false;
            textFieldToType = textField;
            System.out.println("Got");
        } else
            textFieldToType = null;
    }
}
