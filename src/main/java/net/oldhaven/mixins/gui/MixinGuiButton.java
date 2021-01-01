package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButton.class)
public class MixinGuiButton extends Gui {
    @Shadow public int xPosition;
    @Shadow public int yPosition;
    @Shadow protected int height;
    @Shadow protected int width;

    @Inject(method = "drawButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiButton;getHoverState(Z)I", shift = At.Shift.AFTER))
    private void drawButton(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        String f = ModOptions.BUTTON_OUTLINE_HEX.getAsString();
        if(!f.isEmpty()) {
            int color = Integer.decode(f);
            if (i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height)
                drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, adjustAlpha(color, 255));
        }
    }

    @Redirect(method = "drawButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiButton;drawCenteredString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V", ordinal = 2))
    private void redirectStr(GuiButton button, FontRenderer fontRenderer, String s, int i, int i1, int i2) {
        String f = ModOptions.BUTTON_TEXT_HEX.getAsString();
        if(!f.isEmpty()) {
            int color = Integer.decode(f);
            i2 = adjustAlpha(color, 255);
        }
        button.drawCenteredString(fontRenderer, s, i, i1, i2);
    }

    public int adjustAlpha(int rgb, int alpha) {
        int red = (rgb>>16) &0xff;
        int green = (rgb>>8) &0xff;
        int blue = (rgb>>0) &0xff;
        return (alpha&0x0ff)<<24 | red<<16 | green<<8 | blue;
    }
}
