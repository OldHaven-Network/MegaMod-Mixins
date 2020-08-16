package net.oldhaven.mixins.blocks;

import net.minecraft.src.Item;
import net.oldhaven.customs.ItemKeep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {
    private int id;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(int i, CallbackInfo ci) {
        this.id = i;
    }

    @Inject(method = "setItemName", at = @At("RETURN"))
    private void init(String s, CallbackInfoReturnable<Item> cir) {
        ItemKeep.add(s, this.id);
    }
}
