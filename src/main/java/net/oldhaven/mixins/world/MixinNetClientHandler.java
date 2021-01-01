package net.oldhaven.mixins.world;

import net.minecraft.client.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet18Animation;
import net.minecraft.src.Packet3Chat;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.packets.util.Packets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public class MixinNetClientHandler {
    @Shadow private Minecraft mc;

    @Shadow private NetworkManager netManager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        Packets.receiveNetworkClient(this.netManager);
    }

    @Inject(method = "handleChat", at = @At("RETURN"))
    public void handleChat(Packet3Chat var1, CallbackInfo ci) {
        System.out.println(var1.message);
    }

    @Inject(method = "handleArmAnimation", at=@At("HEAD"), cancellable = true)
    private void handleArmAnimation(Packet18Animation packet18Animation, CallbackInfo ci) {
        if(!Packets.canUsePackets())
            if(Packets.initialize(String.valueOf(packet18Animation.animate)))
                ci.cancel();
    }

    @Inject(method = "handleUpdateTime", at=@At("HEAD"), cancellable = true)
    private void handleUpdateTime(CallbackInfo ci) {
        float f = ModOptions.FORCE_TIME.getAsFloat();
        if(f != 0.0F) {
            this.mc.theWorld.setWorldTime((int) (f * 24000.0F));
            ci.cancel();
        }
    }
}
