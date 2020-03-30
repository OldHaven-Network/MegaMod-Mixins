package net.oldhaven.mixins;

import net.minecraft.src.ThreadDownloadResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ThreadDownloadResources.class)
public class MixinThreadDownloadResources {
    @ModifyConstant(method = "run", constant = @Constant(stringValue = "http://s3.amazonaws.com/MinecraftResources/"), remap = false)
    private String getResourcesUrl(String def) {
        return "http://resourceproxy.pymcl.net/MinecraftResources/";
    }
}
