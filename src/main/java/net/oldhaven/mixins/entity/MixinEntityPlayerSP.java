package net.oldhaven.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Session;
import net.minecraft.src.World;
import net.oldhaven.MegaMod;
import net.oldhaven.SkinFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
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
        if(MegaMod.debug)
            var3.username = "ashleez_";
        return SkinFix.getSkinUrl(var3.username);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void EntityPlayerSP(Minecraft minecraft, World world, Session session, int i, CallbackInfo ci) {
        if(session != null && session.username != null && session.username.length() > 0) {
            this.cloakUrl = SkinFix.getCapeUrl(session.username);
        }
    }
}
