package net.oldhaven.mixins.entity;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.oldhaven.customs.SinglePlayerCommands;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.SkinFix;
import net.oldhaven.devpack.SkinImage;
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
        SkinImage skinImage = SkinFix.getCapeUrl(this.username);
        if(skinImage == null || skinImage.hasFailed() || skinImage.imageUrl == null)
            return;
        this.playerCloakUrl = skinImage.imageUrl;
        this.cloakUrl = this.playerCloakUrl;
    }

    @Inject(method = "damageEntity", at = @At("HEAD"), cancellable = true)
    private void damageEntity(int i, CallbackInfo ci) {
        if(!ModOptions.SP_CHEATS.getAsBool())
            return;
        if(!SinglePlayerCommands.isGodded)
            return;
        if(MMUtil.getPlayer().getConnectedServer() == null)
            ci.cancel();
    }
}
