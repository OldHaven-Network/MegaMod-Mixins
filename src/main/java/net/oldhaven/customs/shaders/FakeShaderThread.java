package net.oldhaven.customs.shaders;

import net.minecraft.src.*;
import net.oldhaven.MegaMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeShaderThread implements Runnable {
    public int lightingTick = 0;
    private static Map<Integer, TorchIDs> torchIDs = new HashMap<Integer, TorchIDs>() {
        {
            put(Block.torchRedstoneActive.blockID, new TorchIDs(Block.torchRedstoneActive.blockID, 1.35F));
            put(Block.torchRedstoneIdle.blockID, new TorchIDs(Block.torchRedstoneIdle.blockID, 2.5F));
            put(Block.torchWood.blockID, new TorchIDs(Block.torchWood.blockID, 1.05F));
            put(Block.glowStone.blockID, new TorchIDs(Block.glowStone.blockID, 1.0F));
            put(Block.pumpkinLantern.blockID, new TorchIDs(Block.pumpkinLantern.blockID, 1.8F));
        }
    };
    private static Thread thread;
    private boolean readyToRender = false;
    private List<Vec3D> blocksToRender;
    private IWorld world;
    public FakeShaderThread() {
        blocksToRender = new ArrayList<>();
        //new Thread(this, "renderShaders").start();
    }

    @Override
    public synchronized void run() {
        while(MegaMod.getMinecraftInstance().running) {
            /*try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            World world = MegaMod.getMinecraftInstance().theWorld;
            if(world == null)
                continue;
            EntityPlayerSP player = MegaMod.getMinecraftInstance().thePlayer;
            if (player != null) {
                int to = 10;
                for (int x = -to; x < to; x++) {
                    for (int y = -to; y < to; y++) {
                        for (int z = -to; z < to; z++) {
                            try {
                                world.markBlockNeedsUpdate(x, y, z);
                                world.scheduleLightingUpdate(EnumSkyBlock.Block, x, y, z, x, y, z);
                            } catch (NullPointerException | IndexOutOfBoundsException ignore) {}
                        }
                    }
                }
            }
            for(int i=0;i < blocksToRender.size();i++){
                Vec3D vec3D = blocksToRender.get(i);
                int x = (int) vec3D.xCoord;
                int y = (int) vec3D.yCoord;
                int z = (int) vec3D.zCoord;
                world.markBlockNeedsUpdate(x, y, z);
                blocksToRender.remove(vec3D);
            }*/
        }
    }

    private Vec3D lastLightVec = null;
    public void setWorld(IWorld world) {
        this.world = world;
    }

    public void updateLightingAt(ItemStack heldItem, Vec3D pVec) {
        if(heldItem == null || !torchIDs.containsKey(heldItem.itemID)) {
            if (lastLightVec != null) {
                if(lightingTick > 5) {
                    markExtLightUpdate(lastLightVec);
                    lastLightVec = null;
                }
            }
            return;
        }
        markExtLightUpdate(pVec);
        lastLightVec = pVec;
    }

    private int lastYLight = 0;
    private void markExtLightUpdate(Vec3D lVec) {
        int x = (int) (lVec.xCoord);
        int y = (int) (lVec.yCoord);
        int z = (int) (lVec.zCoord);
        Chunk chunk = ((World) world).getChunkFromBlockCoords(x, z);
        int to = 10;
        for(int i=-to;i < to;i++) {
            for(int o=-to;o < to;o++) {
                Chunk chunk1 = ((World) world).getChunkFromBlockCoords(x+i, z+o);
                if(chunk1 != chunk) {
                    world.markBlockNeedsUpdate(x+i, y, z+o);
                }
            }
        }
        ((World) world).markBlockAsNeedsUpdate(x, y, z);
        ((World) world).markBlockAsNeedsUpdate(x, y-5, z);
    }

    public void cleanup() {
        this.blocksToRender.clear();
    }

    public void addBlockToRender(int x, int y, int z) {
        Vec3D vec3D = Vec3D.createVector(x, y, z);
        this.blocksToRender.add(vec3D);
    }

    private static class TorchIDs {
        private int id;
        private float lower;
        TorchIDs(int i, float lower) {
            this.id = i;
            this.lower = lower;
        }
    }
    private float isHoldingTorch() {
        EntityPlayerSP player = MegaMod.getMinecraftInstance().thePlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        if(stack != null && torchIDs.containsKey(stack.itemID))
            return torchIDs.get(stack.itemID).lower;
        return -1;
    }
    private int calculateLightingI(int x, int y, int z, int originalLight) {
        int i = originalLight;
        if(MegaMod.getMinecraftInstance().thePlayer != null) {
            float lower = isHoldingTorch();
            if(lower == -1)
                return originalLight;
            Vec3D pPos = MegaMod.getMinecraftInstance().thePlayer.getPosition(1.0F);
            Vec3D curPos = Vec3D.createVector(x, y - 1, z);
            double distance = pPos.distanceTo(curPos);
            if (distance < (3.0F/lower))
                i = 15;
            else if(distance < (5.0F/lower))
                i = 7;
            else if(distance < (8.0F/lower))
                i = 2;
        }
        if(i < originalLight)
            i = originalLight+1;
        if(i > 15)
            i = 15;
        return i;
    }
    private float calculateLightingF(int x, int y, int z, float originalLight) {
        float f = originalLight;
        if(MegaMod.getMinecraftInstance().thePlayer != null) {
            float lower = isHoldingTorch();
            if(lower == -1)
                return originalLight;
            Vec3D pPos = MegaMod.getMinecraftInstance().thePlayer.getPosition(1.0F);
            Vec3D curPos = Vec3D.createVector(x, y - 1, z);
            double distance = pPos.distanceTo(curPos);
            if (distance < (3.0F/lower))
                f = 1.0F;
            else if(distance < (5.0F/lower))
                f = 0.55F;
            else if(distance < (8.0F/lower))
                f = 0.1F;
        }
        if(f < originalLight)
            f = originalLight+0.1F;
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }
    public Object calculateLightRender(int x, int y, int z, Object lightSource) {
        System.out.println("Calculate lights");
        boolean isFloat = (lightSource instanceof Float);
        Vec3D vec3D = Vec3D.createVector(x, y, z);
        if(isFloat) {
            float f = calculateLightingF(x, y, z, (float)lightSource);
            if(f != (float)lightSource)
                this.blocksToRender.add(vec3D);
            return f;
        } else {
            int i = calculateLightingI(x, y, z, (int)lightSource);
            if(i != (int)lightSource)
                this.blocksToRender.add(vec3D);
            return i;
        }
    }
}
