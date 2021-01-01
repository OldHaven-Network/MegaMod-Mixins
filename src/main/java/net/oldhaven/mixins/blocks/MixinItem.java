package net.oldhaven.mixins.blocks;

import net.minecraft.src.Entity;
import net.minecraft.src.Item;
import net.oldhaven.customs.ItemKeep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {
    @Shadow protected int iconIndex;

    @Shadow public abstract int getDamageVsEntity(Entity entity);

    private int id;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(int i, CallbackInfo ci) {
        this.id = 256+i;
    }

    @Inject(method = "setItemName", at = @At("RETURN"))
    private void init(String s, CallbackInfoReturnable<Item> cir) {
        ItemKeep.add(s, this.id, 0, this.getDamageVsEntity(null));
    }
}
