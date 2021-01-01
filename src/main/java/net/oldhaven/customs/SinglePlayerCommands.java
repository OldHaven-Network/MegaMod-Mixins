package net.oldhaven.customs;

import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Vec3D;
import net.oldhaven.MMDebug;

import java.util.HashMap;
import java.util.Map;

public class SinglePlayerCommands {
    @FunctionalInterface
    public interface Runnable {
        void run(String[] args, EntityPlayerSP player);
    }

    public static boolean isGodded = false;

    private static void dupeCmd(String cmd, String... names) {
        if(runnableMap.containsKey(cmd)) {
            for(String name : names)
                runnableMap.put(name, runnableMap.get(cmd));
        }
    }

    static void msgPlayer(EntityPlayerSP player, String s) {
        if(MMDebug.enabled)
            System.out.println(s);
        player.addChatMessage(s);
    }
    public static Map<String, Runnable> runnableMap = new HashMap<String, Runnable>(){
        {
            put("help", ((args, player) -> {
                if(args.length > 0) {
                    String cmd = args[0];
                    if(runnableMap.containsKey(cmd.toLowerCase())) {
                        runnableMap.get(cmd.toLowerCase()).run(new String[]{"help"}, player);
                        return;
                    }
                }
                msgPlayer(player, "SP-Commands you can use:");
                msgPlayer(player, " - setspawn, kill, heal");
                msgPlayer(player, " - god, give, time, weather");
            }));
            put("setspawn", (args, player) -> {
                if(args.length > 0 && args[0].toLowerCase().equals("help")) {
                    msgPlayer(player, "SetSpawn: Sets your spawn to the current location");
                    return;
                }
                Vec3D loc = player.getPosition(1.0F);
                player.worldObj.getWorldInfo().setSpawnX((int)loc.xCoord);
                player.worldObj.getWorldInfo().setSpawnY((int)loc.yCoord);
                player.worldObj.getWorldInfo().setSpawnZ((int)loc.zCoord);
                msgPlayer(player, "Set spawn location to current coords");
            });
            put("kill", (args, player) -> {
                if(args.length > 0 && args[0].toLowerCase().equals("help")) {
                    msgPlayer(player, "Kill: Just kills you");
                    return;
                }
                msgPlayer(player, "F");
                player.setHealth(0);
            });
            put("god", ((args, player) -> {
                isGodded = !isGodded;
                msgPlayer(player, "You are no" + (isGodded ? "w in GodMode" : " longer in GodMode"));
            }));
            put("heal", (args, player) -> {
                if(args.length > 0 && args[0].toLowerCase().equals("help")) {
                    msgPlayer(player, "Heal: Gives you all your health back");
                    return;
                }
                msgPlayer(player, "Healed!");
                player.setHealth(20);
            });
            put("give", (args, player) -> {
                if(args.length < 1 || args[0].toLowerCase().equals("help")) {
                    msgPlayer(player, "/give <item> <amount#>");
                    return;
                }
                String item = args[0].toLowerCase();
                int amount = 1;
                if(args.length > 1) {
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch(NumberFormatException ignored) {
                        msgPlayer(player, "Invalid amount");
                        msgPlayer(player, "/give <item> <amount#>");
                    }
                }
                int block = ItemKeep.get(item);
                if(block != 0) {
                    ItemStack stack = new ItemStack(block, amount, 0);
                    player.inventory.addItemStackToInventory(new ItemStack(block, amount, 0));
                    msgPlayer(player, "Gave you "+amount+ " of "+stack.getItemName());
                }
                msgPlayer(player, "Invalid block ID or name");
            });
            put("time", (args, player) -> {
                if(args.length < 1 || args[0].toLowerCase().equals("help")) {
                    msgPlayer(player, "/time <day/noon/sunset/night/sunrise/TICK#>");
                    return;
                }
                String time = args[0].toLowerCase();
                int timeToSet = 0;
                switch(time) {
                    case "day":
                        timeToSet = 1000; break;
                    case "noon":
                        timeToSet = 6000; break;
                    case "sunset":
                        timeToSet = 12000; break;
                    case "night":
                        timeToSet = 13000; break;
                    case "sunrise":
                        timeToSet = 23000; break;
                    default:
                        try {
                            timeToSet = Integer.parseInt(time);
                        } catch(NumberFormatException ignored) {
                            msgPlayer(player, "Invalid time");
                            msgPlayer(player, "/time <day/noon/sunset/night/sunrise/TICK#>");
                            return;
                        }
                        break;
                }
                player.worldObj.getWorldInfo().setWorldTime(timeToSet);
                msgPlayer(player, "Set the world time to " + timeToSet + " ticks");
            });
            put("weather", (args, player) -> {
                if(args.length < 1 || args[0].toLowerCase().equals("help")) {
                    msgPlayer(player, "/weather <rain/snow/thunder/clear/sunny>");
                    return;
                }
                String type = args[0].toLowerCase();
                switch(type) {
                    case "bad":
                    case "raining":
                    case "rain":
                    case "snow":
                    case "snowing":
                        player.worldObj.getWorldInfo().setRaining(true);
                        msgPlayer(player, "Set weather to raining");
                        break;
                    case "thundering":
                    case "thunder":
                        player.worldObj.getWorldInfo().setRaining(true);
                        player.worldObj.getWorldInfo().setThundering(true);
                        msgPlayer(player, "Set weather to rain & thunder");
                        break;
                    default:
                        player.worldObj.getWorldInfo().setRaining(false);
                        player.worldObj.getWorldInfo().setThundering(false);
                        msgPlayer(player, "Set weather to sunny");
                        break;
                }
            });
        }
    };
    static {
        dupeCmd("heal", "health", "heals");
        dupeCmd("setspawn", "spawnpoint");
        dupeCmd("god", "godmode");
        dupeCmd("time", "settime");
        dupeCmd("help", "?");
        System.out.println("Created " + runnableMap.size() + " SinglePlayer Cmds\n");
    }
}
