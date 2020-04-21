package net.oldhaven.mixins.entity;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.oldhaven.SkinFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer extends EntityLiving {
    @Shadow public String playerCloakUrl;
    @Shadow public String username;

    public MixinEntityPlayer(World world) {
        super(world);
    }

    @Inject(method = "updateCloak", at = @At("RETURN"))
    private void updateCloak(CallbackInfo ci) {
        this.playerCloakUrl = SkinFix.getCapeUrl(this.username);
        this.cloakUrl = this.playerCloakUrl;
    }
}
