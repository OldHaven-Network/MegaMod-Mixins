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
    @Shadow public abstract ItemStack getCurrentEquippedItem();

    public MixinEntityPlayer(World world) {
        super(world);
    }

    int i = 0;
    @Inject(method = "onUpdate", at=@At("INVOKE"))
    public void onUpdate(CallbackInfo ci) {
        if(ModOptions.DYNAMIC_LIGHTING.getAsInt() == 1) {
            if(i > 5) {
                MegaMod.getFakeShaderThread().updateLightingAt(this.getCurrentEquippedItem(), this.getPosition(1.0F));
                i = 0;
            }
            i++;
        }
    }
}
