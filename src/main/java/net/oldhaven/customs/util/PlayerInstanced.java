package net.oldhaven.customs.util;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Vec3D;
import net.oldhaven.MegaModDiscord;
import net.oldhaven.customs.alexskins.CustomRenderPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerInstanced {
    public final CustomRenderPlayer customRenderPlayer;
    private final List<String> joinedNames;
    private String connectedServer;
    public Entity pointingEntity = null;
    PlayerInstanced(CustomRenderPlayer crp) {
        this.customRenderPlayer = crp;
        this.joinedNames = new ArrayList<>();
    }
    public EntityPlayerSP getPlayerSP() {
        return MMUtil.getMinecraftInstance().thePlayer;
    }
    public boolean doesPlayerExist() {
        return MMUtil.getMinecraftInstance().thePlayer != null;
    }
    public double getPlayerSpeed() {
        if(!doesPlayerExist())
            return -1;
        EntityPlayerSP p = getPlayerSP();
        return Vec3D.createVector(p.posX, p.posY, p.posZ).distanceTo(Vec3D.createVector(p.lastTickPosX, p.lastTickPosY, p.lastTickPosZ));
    }
    public double getPlayerMotion() {
        if(!doesPlayerExist())
            return -1;
        EntityPlayerSP p = getPlayerSP();
        return (p.motionX * p.motionX + p.motionZ * p.motionZ);
    }

    public void addPlayerJoin(String name) {
        joinedNames.add(name);
        MegaModDiscord.setState("With " + joinedNames.size() + " players");
        MegaModDiscord.updatePresence();
    }
    public void removePlayerJoin(String name) {
        joinedNames.remove(name);
        MegaModDiscord.setState("With " + joinedNames.size() + " players");
        MegaModDiscord.updatePresence();
    }
    public List<String> getJoinedNames() {
        return joinedNames;
    }
    public void clearJoinedNames() {
        joinedNames.clear();
    }

    public void setConnectedServer(String s) {
        connectedServer = s;
        if(s != null) {
            String server = s.replaceAll("\\.", "").toLowerCase().split(":")[0];
            String noport = s.split(":")[0];
            for(MegaModDiscord.Images images : MegaModDiscord.Images.values()) {
                if(images.name().toLowerCase().equals(server)) {
                    MegaModDiscord.setImages(images);
                    break;
                }
            }
            MegaModDiscord.setDetails("Playing " + noport);
            MegaModDiscord.setState("");
        } else {
            MegaModDiscord.setDetails("In main menu");
            MegaModDiscord.setState("");
            MegaModDiscord.setImages(MegaModDiscord.Images.MainMenu);
        }
        MegaModDiscord.updatePresence();
    }
    public String getConnectedServer() {
        return connectedServer;
    }
}
