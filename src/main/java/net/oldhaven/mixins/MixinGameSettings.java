package net.oldhaven.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.oldhaven.customs.util.MMUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameSettings.class)
public class MixinGameSettings {
    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At("RETURN"))
    private void onInit(Minecraft var1, File var2, CallbackInfo ci) {
        MMUtil.getAutoLogins().savedLoginsFile = new File(var2, "savedLogins.txt");
        MMUtil.getAutoLogins().readLogins();
        MMUtil.getSavedServers().savedServersFile = new File(var2, "savedServers.txt");
        MMUtil.getSavedServers().readServers();
        MMUtil.getCustomGameSettings().optionsFile = new File(var2, "megamodOptions.ini");
        MMUtil.getCustomGameSettings().readSettings();
        MMUtil.getCustomKeybinds().savedKeysFile = new File(var2, "savedKeys.txt");
        MMUtil.getCustomKeybinds().loadKeys();
        MMUtil.getSavedShaders().shaderFolder = new File(var2, "shaders/");
        MMUtil.getSavedShaders().loadShaders();
    }
}
