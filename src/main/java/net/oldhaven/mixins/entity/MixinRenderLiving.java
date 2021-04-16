package net.oldhaven.mixins.entity;

import net.minecraft.src.*;
import net.oldhaven.customs.alexskins.CustomModelBiped;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.SkinFix;
import net.oldhaven.devpack.SkinImage;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;

@Mixin(RenderLiving.class)
public class MixinRenderLiving {
    @Shadow protected ModelBase mainModel;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ModelBase modelBase, float v, CallbackInfo ci) {
        if(modelBase instanceof ModelBiped && !(modelBase instanceof ModelZombie)) {
            this.mainModel = new CustomModelBiped(0, 0);
        }
    }

    @Redirect(method = "doRenderLiving", at=@At(
                    value = "INVOKE",
                    target= "Lnet/minecraft/src/RenderLiving;loadDownloadableImageTexture(Ljava/lang/String;Ljava/lang/String;)Z",
                    opcode = Opcodes.INVOKEVIRTUAL
    ))
    private boolean renderSkin(RenderLiving renderLiving, String url, String tex)  {
        RenderEngine engine = MMUtil.getMinecraftInstance().renderEngine;
        SkinImage skinImage = SkinFix.getSkinUrl(url);
        int i = -1;
        if(skinImage == null)
            i = engine.getTexture(tex);
        else if(skinImage.failed == null || skinImage.hasFailed())
            i = engine.getTexture(tex);
        else {
            BufferedImage image = skinImage.image;
            i = engine.allocateAndSetupTexture(image);
        }
        engine.bindTexture(i);
        return true;
    }
}
