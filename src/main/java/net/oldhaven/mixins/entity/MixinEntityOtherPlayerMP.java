package net.oldhaven.mixins.entity;

import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.oldhaven.customs.util.SkinFix;
import net.oldhaven.devpack.SkinImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityOtherPlayerMP.class)
public abstract class MixinEntityOtherPlayerMP extends EntityPlayer {
    public MixinEntityOtherPlayerMP(World world) {
        super(world);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;toString()Ljava/lang/String;"))
    private String toStr(StringBuilder builder, World var2, String var3) {
        SkinFix.UserSkin userSkin = SkinFix.getUserSkin(var3);
        if(userSkin == null)
            return null;
        return userSkin.skinUrl;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void EntityOtherPlayerMP(World world, String name, CallbackInfo ci) {
        if(name != null && name.length() > 0) {
            SkinImage skinImage = SkinFix.getCapeUrl(name);
            if(skinImage == null || skinImage.hasFailed() || skinImage.imageUrl == null)
                return;
            this.cloakUrl = skinImage.imageUrl;
        }
    }
}
