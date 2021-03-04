package net.oldhaven.mixins.blocks;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.TileEntitySignRenderer;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntitySignRenderer.class)
public class MixinTileEntitySignRenderer {
    @Redirect(method = "renderTileEntitySignAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FontRenderer;drawString(Ljava/lang/String;III)V", ordinal = 0))
    private void renderText0(FontRenderer renderer, String s, int i, int i1, int i2) {
        drawText(renderer, s, i, i1, i2);
    }
    @Redirect(method = "renderTileEntitySignAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/FontRenderer;drawString(Ljava/lang/String;III)V", ordinal = 1))
    private void renderText1(FontRenderer renderer, String s, int i, int i1, int i2) {
        drawText(renderer, s, i, i1, i2);
    }

    private void drawText(FontRenderer renderer, String s, int i, int i1, int i2) {
        if(ModOptions.SIGNS_TEXT_SHADOW.getAsBool()) {
            /*if (i2 == 0x000000) {
                renderer.drawString(s, i + 1, i1 + 1, 0x555555);
                renderer.drawString(s, i, i1, i2);
            } else*/
            renderer.drawStringWithShadow(s, i, i1, i2);
        } else
            renderer.drawString(s, i, i1, i2);
    }
}
