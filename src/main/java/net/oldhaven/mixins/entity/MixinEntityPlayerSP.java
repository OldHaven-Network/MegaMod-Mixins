package net.oldhaven.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Session;
import net.minecraft.src.World;
import net.oldhaven.MMDebug;
import net.oldhaven.customs.util.SkinFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends EntityPlayer {
    public MixinEntityPlayerSP(World world) {
        super(world);
    }

    @Override
    public void func_6420_o() {

    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;toString()Ljava/lang/String;"))
    private String toStr(StringBuilder builder, Minecraft var1, World var2, Session var3, int var4) {
        if(MMDebug.enabled)
            var3.username = MMDebug.debugUserName;
        SkinFix.UserSkin userSkin = SkinFix.getUserSkin(var3.username);
        if(userSkin == null)
            return null;
        return userSkin.skinUrl;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void EntityPlayerSP(Minecraft minecraft, World world, Session session, int i, CallbackInfo ci) {
        if(session != null && session.username != null && session.username.length() > 0) {
            this.cloakUrl = SkinFix.getCapeUrl(session.username);
        }
    }
}
