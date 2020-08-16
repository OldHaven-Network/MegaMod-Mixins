package net.oldhaven.mixins.blocks;

import net.minecraft.src.Block;
import net.oldhaven.customs.ItemKeep;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Shadow @Final public int blockID;

    @Inject(method = "setBlockName", at = @At("RETURN"))
    private void init(String s, CallbackInfoReturnable<Block> cir) {
        ItemKeep.add(s, this.blockID);
    }
}
