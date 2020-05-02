package net.oldhaven.mixins.shader.dynamiclighting;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLiving {
    public MixinEntityPlayer(World world) {
        super(world);
    }

    @Shadow public abstract ItemStack getCurrentEquippedItem();

    @Inject(method = "onUpdate", at=@At("INVOKE"))
    public void onUpdate(CallbackInfo ci) {
        if(ModOptions.DYNAMIC_LIGHTING.getAsInt() == 1) {
            int i = MegaMod.getFakeShaderThread().lightingTick;
            if(i > 5) {
                MegaMod.getFakeShaderThread().updateLightingAt(this.getCurrentEquippedItem(), this.getPosition(1.0F));
                i = 0;
            }
            MegaMod.getFakeShaderThread().lightingTick = i+1;
        }
    }
}
