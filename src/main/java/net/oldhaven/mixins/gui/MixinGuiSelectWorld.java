package net.oldhaven.mixins.gui;

import net.minecraft.src.GuiSelectWorld;
import net.oldhaven.MegaModDiscord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSelectWorld.class)
public class MixinGuiSelectWorld {
    @Inject(method = "selectWorld", at=@At(value = "INVOKE", target = "Lnet/minecraft/src/GuiSelectWorld;getSaveFileName(I)Ljava/lang/String;", shift = At.Shift.AFTER))
    private void selectWorld(int v, CallbackInfo ci) {
        MegaModDiscord.setImages(MegaModDiscord.Images.SinglePlayer);
        MegaModDiscord.setDetails("Playing SinglePlayer");
        MegaModDiscord.setParty("With self", 1, 1);
        MegaModDiscord.updatePresence();
    }
}
