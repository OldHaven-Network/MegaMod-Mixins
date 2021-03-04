package net.oldhaven.mixins.shader.dynamiclighting;

import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
    @Shadow public abstract int getBlockLightValue_do(int i, int i1, int i2, boolean b);

    @Shadow @Final public WorldProvider worldProvider;

    @Shadow public abstract int getBlockLightValue(int i, int i1, int i2);

    @Shadow public abstract void neighborLightPropagationChanged(EnumSkyBlock enumSkyBlock, int i, int i1, int i2, int i3);

    @Inject(method = "getLightBrightness", at = @At("INVOKE"), cancellable = true)
    private void getLightBrightness(int x, int y, int z, CallbackInfoReturnable<Float> cir) {
        int shaders = (int)(ModOptions.SHADERS.getAsFloat()*ModOptions.SHADERS.getTimes());
        if(shaders <= 1 || !ModOptions.DYNAMIC_LIGHTING.getAsBool())
            return;
        Object i = MMUtil.getFakeShaderThread().calculateLightRender(x, y, z, this.worldProvider.lightBrightnessTable[this.getBlockLightValue(x, y, z)]);
        if((float)i != 0.0F)
            cir.setReturnValue((float)i);
    }

    @Inject(method = "getBlockLightValue", at = @At("INVOKE"), cancellable = true)
    private void getLightValue(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        int shaders = (int)(ModOptions.SHADERS.getAsFloat()*ModOptions.SHADERS.getTimes());
        if(shaders <= 1 || !ModOptions.DYNAMIC_LIGHTING.getAsBool())
            return;
        Object i = MMUtil.getFakeShaderThread().calculateLightRender(x, y, z, this.getBlockLightValue_do(x, y, z, true));
        if((int)i != 0)
            cir.setReturnValue((int)i);
    }

    @Inject(method = "getFullBlockLightValue", at=@At("INVOKE"), cancellable = true)
    private void getFullBlockLightValue(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        int shaders = (int)(ModOptions.SHADERS.getAsFloat()*ModOptions.SHADERS.getTimes());
        if(shaders <= 1 || !ModOptions.DYNAMIC_LIGHTING.getAsBool())
            return;
        Object i = MMUtil.getFakeShaderThread().calculateLightRender(x, y, z, this.getBlockLightValue_do(x, y, z, true));
        if((int)i != 0)
            cir.setReturnValue((int)i);
    }
}
