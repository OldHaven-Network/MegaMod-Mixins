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
        MegaMod.getInstance().getAutoLogins().savedLoginsFile = new File(var2, "savedLogins.txt");
        MegaMod.getInstance().getAutoLogins().readLogins();
        MegaMod.getInstance().getSavedServers().savedServersFile = new File(var2, "savedServers.txt");
        MegaMod.getInstance().getSavedServers().readServers();
        MegaMod.getInstance().getCustomGameSettings().optionsFile = new File(var2, "megamodOptions.txt");
        MegaMod.getInstance().getCustomGameSettings().readSettings();
        MegaMod.getInstance().getCustomKeybinds().savedKeysFile = new File(var2, "savedKeys.txt");
        MegaMod.getInstance().getCustomKeybinds().loadKeys();
    }
}
