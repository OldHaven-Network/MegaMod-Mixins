package net.oldhaven.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnecting;
import net.oldhaven.customs.util.MMUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void GuiConnecting(Minecraft minecraft, String s, int i, CallbackInfo ci) {
        System.out.println("set connected server " + (s + ":"+ i));
        MMUtil.getPlayer().setConnectedServer(s + ":" + i);
    }
}
