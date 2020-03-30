package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.CustomGameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButton.class)
public class MixinGuiButton extends Gui {
    @Shadow public int xPosition;
    @Shadow public int yPosition;
    @Shadow protected int height;
    @Shadow protected int width;

    @Inject(method = "drawButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiButton;getHoverState(Z)I", shift = At.Shift.AFTER))
    private void drawButton(Minecraft minecraft, int i, int j, CallbackInfo ci) {
        CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
        int value = (int)(gs.getOptionF("Button Outline") * 10.0F);
        if(value > 0) {
            int color;
            switch(value) {
                case 2: color = 0x3232a8;break;
                case 3: color = 0xa032a8;break;
                case 4: color = 0xa8324e;break;
                case 5: color = 0x32a2a8;break;
                case 6: color = 0x32a86d;break;
                case 7: color = 0xa8a432;break;
                case 8: color = 0xa87532;break;
                case 9: color = 0xbdbdbd;break;
                case 10: color = 0x636363;break;
                case 11: color = 0x000000;break;
                default: color = 0xffffff;break;
            }
            if (i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height)
                drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, adjustAlpha(color, 255));
        }
    }

    public int adjustAlpha(int rgb, int alpha) {
        int red = (rgb>>16) &0xff;
        int green = (rgb>>8) &0xff;
        int blue = (rgb>>0) &0xff;
        return (alpha&0x0ff)<<24 | red<<16 | green<<8 | blue;
    }
}
