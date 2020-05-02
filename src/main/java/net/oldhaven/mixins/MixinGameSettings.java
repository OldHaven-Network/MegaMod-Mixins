package net.oldhaven.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.oldhaven.MegaMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameSettings.class)
public class MixinGameSettings {
    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At("RETURN"))
    private void onInit(Minecraft var1, File var2, CallbackInfo ci) {
        MegaMod.getAutoLogins().savedLoginsFile = new File(var2, "savedLogins.txt");
        MegaMod.getAutoLogins().readLogins();
        MegaMod.getSavedServers().savedServersFile = new File(var2, "savedServers.txt");
        MegaMod.getSavedServers().readServers();
        MegaMod.getCustomGameSettings().optionsFile = new File(var2, "megamodOptions.ini");
        MegaMod.getCustomGameSettings().readSettings();
        MegaMod.getCustomKeybinds().savedKeysFile = new File(var2, "savedKeys.txt");
        MegaMod.getCustomKeybinds().loadKeys();
        MegaMod.getSavedShaders().shaderFolder = new File(var2, "shaders/");
        MegaMod.getSavedShaders().loadShaders();
    }
}
