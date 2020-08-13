package net.oldhaven.customs;

import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Vec3D;

import java.util.HashMap;
import java.util.Map;

public class SinglePlayerCommands {
    @FunctionalInterface
    public interface Runnable {
        void run(String[] args, EntityPlayerSP player);
    }
    public static Map<String, Runnable> runnableMap = new HashMap<String, Runnable>(){
        {
            put("setspawn", (args, player) -> {
                Vec3D loc = player.getPosition(1.0F);
                player.worldObj.getWorldInfo().setSpawnX((int)loc.xCoord);
                player.worldObj.getWorldInfo().setSpawnY((int)loc.yCoord);
                player.worldObj.getWorldInfo().setSpawnZ((int)loc.zCoord);
            });
            put("kill", (args, player) -> {
                player.setHealth(0);
            });
            put("heal", (args, player) -> {
                player.setHealth(20);
            });
            put("give", (args, player) -> {
                if(args.length < 1)
                    return;
                String item = args[0];
            });
            put("coop", (args, player) -> {
                player.sendChatMessage("coop moment");
            });
        }
    };
}
