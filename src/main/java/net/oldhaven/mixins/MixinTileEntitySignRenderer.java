package net.oldhaven.mixins;

import net.minecraft.src.TileEntitySign;
import net.minecraft.src.TileEntitySignRenderer;
import net.oldhaven.MegaMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(TileEntitySignRenderer.class)
public class MixinTileEntitySignRenderer {
    @Redirect(
            method = "renderTileEntitySignAt",
            at = @At(value = "INVOKE",target = "Ljava/lang/StringBuilder;toString()Ljava/lang/String;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "L_Dummy_$__Array__;length:I", ordinal = 0)))
    private String redirect2(StringBuilder builder, TileEntitySign var1, double var2, double var4, double var6, float var8) {
        String text = var1.signText[var1.lineBeingEdited];
        int cursor = MegaMod.getInstance().signCursorLoc;
        return text.substring(0, cursor) + "|" + text.substring(cursor);
    }
}
