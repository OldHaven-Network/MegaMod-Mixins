package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.gui.controls.GuiControlsSlot;
import net.oldhaven.gui.controls.IGuiControls;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiControls.class)
public abstract class MixinGuiControls extends GuiScreen implements IGuiControls {
    private GuiControlsSlot slot;
    private List<GuiButton> guiButtons;
    @Shadow private int buttonId;
    @Shadow private GameSettings options;
    @Shadow private GuiScreen parentScreen;

    @Override
    public void drawDefaultBackground() {
        super.drawBackground(0);
    }

    @Override
    public void drawString(FontRenderer var1, String var2, int var3, int var4, int var5) {
        super.drawString(var1, var2, var3, var4, var5);
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        List<GuiButton> keepers = new ArrayList<>();
        List<GuiButton> self = new ArrayList<>();
        for(GuiButton button : (List<GuiButton>) this.controlList) {
            if(button.id == 200) {
                button.yPosition = height - 26;
                self.add(button);
            } else
                keepers.add(button);
        }
        this.controlList.clear();;
        this.controlList.addAll(self);
        slot = new GuiControlsSlot(this, this.mc, this.width, this.height, 32, (this.height -55) + 4, 36, keepers);
        slot.registerScrollButtons(controlList, 7, 8);
    }

    public void onAction(GuiButton guiButton, GuiControlsSlot.ButtonInfo isNew) {
        this.clickedButtonIsNew = isNew;
        this.actionPerformed(guiButton);
    }

    private GuiControlsSlot.ButtonInfo clickedButtonIsNew = null;
    private GuiButton clickedButton;
    /**
     * @author cutezyash
     * @reason New controls screen
     */
    @Overwrite
    public void keyTyped(char var1, int var2) {
        if (this.clickedButton != null) {
            if(clickedButtonIsNew == null) {
                this.options.setKeyBinding(this.clickedButton.id, var2);
            } else
                MegaMod.getCustomKeybinds().setKey(clickedButtonIsNew.getString(), var2);
            clickedButton.displayString = Keyboard.getKeyName(var2);
            this.clickedButton = null;
            this.clickedButtonIsNew = null;
            this.lastButton = null;
        } else {
            super.keyTyped(var1, var2);
        }
    }


    private GuiButton lastButton;
    /**
     * @author cutezyash
     * @reason New controls screen
     */
    @Overwrite
    public void actionPerformed(GuiButton var1) {
        if (var1.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else {
            if(lastButton != null) {
                if(lastButton.id == var1.id)
                    return;
                int lI = lastButton.displayString.indexOf("<", 3)-1;
                if(lI != -2)
                    lastButton.displayString = lastButton.displayString.substring(2, lI);
            }
            this.lastButton = var1;
            this.clickedButton = var1;
            /*if(!this.clickedButtonIsNew) {
                var1.displayString = "> " + this.options.getOptionDisplayString(var1.id) + " <";
            } else {*/
                var1.displayString = "> " + var1.displayString + " <";
            //}
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiControls;drawDefaultBackground()V"))
    private void drawScreen(GuiControls guiControls, int i, int j, float f) {
        slot.drawScreen(i, j, f);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GameSettings;getKeyBindingDescription(I)Ljava/lang/String;"))
    private String drawStrings(GameSettings settings, int i) {
        return "";
    }
}
