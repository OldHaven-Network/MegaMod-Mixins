package net.oldhaven.mixins.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.oldhaven.customs.ItemKeep;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock {
    @Shadow @Final public int blockID;
    @Shadow protected float blockHardness;

    @Inject(method = "setBlockName", at = @At("RETURN"))
    private void init(String s, CallbackInfoReturnable<Block> cir) {
        ItemKeep.add(s, this.blockID, this.blockHardness, -1);
    }

    @Redirect(method = "getBlockBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/IBlockAccess;getBrightness(IIII)F"))
    private float getBrightness(IBlockAccess iBlockAccess, int i, int i1, int i2, int i3) {
        float bright = ModOptions.WORLD_BRIGHTNESS.getAsFloat();
        return iBlockAccess.getBrightness(i, i1, i2, i3)+(bright/5);
    }
}
