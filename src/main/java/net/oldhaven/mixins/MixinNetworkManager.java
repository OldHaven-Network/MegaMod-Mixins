package net.oldhaven.mixins;

import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.Socket;
import java.net.SocketException;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/net/Socket;setTrafficClass(I)V"))
    private void redirect(Socket socket, int tc) {
        try {
            socket.setTrafficClass(24);
            socket.setTcpNoDelay(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
