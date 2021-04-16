package net.oldhaven.mixins.blocks;

import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.gui.onscreen.MiniMapUI;
import net.oldhaven.gui.onscreen.OnScreenUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(RenderBlocks.class)
public abstract class MixinRenderBlocks {
    @Shadow private int overrideBlockTexture;
    @Shadow private IBlockAccess blockAccess;
    @Shadow private boolean renderAllFaces;
    @Shadow public abstract void renderBottomFace(Block block, double v, double v1, double v2, int i);
    @Shadow public abstract void renderTopFace(Block block, double v, double v1, double v2, int i);

    @Shadow private float colorBlueTopRight;
    @Shadow private float colorGreenTopRight;
    @Shadow private float colorRedTopRight;
    @Shadow private float colorBlueBottomRight;
    @Shadow private float colorGreenBottomRight;
    @Shadow private float colorRedBottomRight;
    @Shadow private float colorBlueBottomLeft;
    @Shadow private float colorGreenBottomLeft;
    @Shadow private float colorRedBottomLeft;
    @Shadow private float colorBlueTopLeft;
    @Shadow private float colorGreenTopLeft;
    @Shadow private float colorRedTopLeft;
    @Shadow private float aoLightValueXPos;
    @Shadow private float field_22356_D;
    @Shadow private float field_22371_s;
    @Shadow private float field_22372_r;
    @Shadow private float field_22364_z;
    @Shadow private float field_22353_F;
    @Shadow private float field_22370_t;
    @Shadow private float field_22360_B;
    @Shadow private float field_22365_y;
    @Shadow private boolean field_22334_Y;
    @Shadow private boolean field_22338_U;
    @Shadow private boolean field_22363_aa;
    @Shadow private boolean field_22359_ac;
    @Shadow private int field_22352_G;

    @Shadow public abstract void renderNorthFace(Block block, double v, double v1, double v2, int i);

    @Shadow public static boolean fancyGrass;
    @Shadow private float aoLightValueXNeg;
    @Shadow private float field_22358_C;
    @Shadow private float field_22369_u;
    @Shadow private float field_22368_v;
    @Shadow private float field_22367_w;
    @Shadow private float field_22354_E;
    @Shadow private float field_22375_o;
    @Shadow private float field_22376_n;
    @Shadow private float field_22377_m;
    @Shadow private boolean field_22337_V;
    @Shadow private boolean field_22333_Z;
    @Shadow private boolean field_22357_ad;
    @Shadow private boolean field_22335_X;

    @Shadow public abstract void renderWestFace(Block block, double v, double v1, double v2, int i);

    @Shadow private float aoLightValueZPos;
    @Shadow private float field_22362_A;
    @Shadow private float field_22373_q;
    @Shadow private boolean field_22336_W;
    @Shadow private boolean field_22355_ae;
    @Shadow private float aoLightValueZNeg;
    @Shadow private float field_22374_p;
    @Shadow private float field_22366_x;

    @Shadow public abstract void renderEastFace(Block block, double v, double v1, double v2, int i);

    @Shadow private boolean field_22361_ab;
    @Shadow private boolean field_22339_T;
    @Shadow private float aoLightValueYPos;
    @Shadow private float aoLightValueYNeg;
    @Shadow private float lightValueOwn;
    @Shadow private boolean enableAO;

    @Shadow public abstract void renderSouthFace(Block block, double v, double v1, double v2, int i);

    @Shadow public abstract void renderCrossedSquares(Block block, int i, double v, double v1, double v2);

    @Shadow public abstract void func_1245_b(Block block, int i, double v, double v1, double v2);

    private boolean[] lastRendering;
    @Inject(method = "<init>(Lnet/minecraft/src/IBlockAccess;)V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        lastRendering = new boolean[]{false};
    }

    @Inject(method = "renderBlockByRenderType", at = @At("HEAD"), cancellable = true)
    public void renderBlockByRenderType(Block var1, int var2, int var3, int var4, CallbackInfoReturnable<Boolean> ci) {
        boolean fancyTress = (ModOptions.FANCY_TREES.getAsInt() > 0);
        if(fancyTress && var1.blockID == 18) {
            ci.setReturnValue(renderBlockLeaves(var1, var2, var3, var4));
        }
    }

    @Redirect(method = "renderBlockFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;colorMultiplier(Lnet/minecraft/src/IBlockAccess;III)I"))
    private int redirect(Block block, IBlockAccess iBlockAccess, int i, int i1, int i2) {
        int id = iBlockAccess.getBlockId(i, i1, i2);
        if(id == Block.waterStill.blockID || id == Block.waterMoving.blockID) {
            String s = ModOptions.WATER_COLOR.getAsString();
            if(!s.isEmpty())
                return Integer.decode(ModOptions.WATER_COLOR.getAsString());
        } else if(id == Block.lavaStill.blockID || id == Block.lavaMoving.blockID) {
            String s = ModOptions.LAVA_COLOR.getAsString();
            if(!s.isEmpty())
                return Integer.decode(ModOptions.LAVA_COLOR.getAsString());
        }
        return block.colorMultiplier(iBlockAccess, i, i1, i2);
    }

    @Redirect(method = "renderBlockReed", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderCrossedSquares(Lnet/minecraft/src/Block;IDDD)V"))
    public void renderBlockGrass(RenderBlocks renderBlocks, Block block, int meta, double x, double y, double z) {
        renderCrossedSquares(block, meta, x, y, z);
        if(!ModOptions.RANDOM_TALLERGRASS.getAsBool())
            return;
        if(block == Block.tallGrass)
            renderTallGrass(block, meta, x, y, z, true, 0.85F, 0.05F);
    }

    private int rand(int seed, int x, int y) {
        int h = seed + x*374761393 + y*668265263; //all constants are prime
        h = (h^(h >> 13))*1274126177;
        return h^(h >> 16);
    }

    public void renderTallGrass(Block block, int meta, double x, double y, double z, boolean up, double times, double timesUp) {
        if(up) {
            TileEntity tileEntity = blockAccess.getBlockTileEntity((int) x, (int) y + 1, (int) z);
            if (tileEntity != null)
                return;
        }
        boolean b = rand(128568126, (int)x, (int)z)/100000 < 1500;
        if(b) {
            ThreadLocalRandom ran = ThreadLocalRandom.current();
            x += (ran.nextDouble()*timesUp) + (ran.nextDouble()*(timesUp+timesUp));
            z += (ran.nextDouble()*timesUp) - (ran.nextDouble()*(timesUp+timesUp));
            if(up)
                y += 0.75F;
            renderCrossedSquares(block, meta, x, y, z);
        }
    }

    public void func_1245_b(Block block, int i, double d, double d1, double d2, boolean random)
    {
        Tessellator tessellator = Tessellator.instance;
        int j = block.getBlockTextureFromSideAndMetadata(0, i);
        if(overrideBlockTexture >= 0)
            j = overrideBlockTexture;

        double x = 0;
        double y = 0;
        double z = 0;
        if(random) {
            double times = 0.15F;
            ThreadLocalRandom ran = ThreadLocalRandom.current();
            x = (ran.nextDouble()*times)-(ran.nextDouble()*(times+times));
            y = (ran.nextDouble()*times)-(ran.nextDouble()*(times+times));
            z = (ran.nextDouble()*times)-(ran.nextDouble()*(times+times));
        }

        int k = (j & 0xf) << 4;
        int l = j & 0xf0;
        double d3 = (float)k / 256F;
        double d4 = ((float)k + 15.99F) / 256F;
        double d5 = (float)l / 256F;
        double d6 = ((float)l + 15.99F) / 256F;
        double d7 = (d + 0.5D) - 0.25D;
        double d8 = d + 0.5D + 0.25D;
        double d9 = (d2 + 0.5D) - 0.5D;
        double d10 = d2 + 0.5D + 0.5D;
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d9+z, d3, d5);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d9+z, d3, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d10+z, d4, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d10+z, d4, d5);
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d10+z, d3, d5);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d10+z, d3, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d9+z, d4, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d9+z, d4, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d10+z, d3, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d10+z, d3, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d9+z, d4, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d9+z, d4, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d9+z, d3, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d9+z, d3, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d10+z, d4, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d10+z, d4, d5);
        d7 = (d + 0.5D) - 0.5D;
        d8 = d + 0.5D + 0.5D;
        d9 = (d2 + 0.5D) - 0.25D;
        d10 = d2 + 0.5D + 0.25D;
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d9+z, d3, d5);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d9+z, d3, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d9+z, d4, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d9+z, d4, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d9+z, d3, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d9+z, d3, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d9+z, d4, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d9+z, d4, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d10+z, d3, d5);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d10+z, d3, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d10+z, d4, d6);
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d10+z, d4, d5);
        tessellator.addVertexWithUV(d7+x, d1 + 1.0D-y, d10+z, d3, d5);
        tessellator.addVertexWithUV(d7+x, d1 + 0.0D+y, d10+z, d3, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 0.0D+y, d10+z, d4, d6);
        tessellator.addVertexWithUV(d8+x, d1 + 1.0D-y, d10+z, d4, d5);
    }

    public boolean renderBlockLeaves(Block block, int i, int j, int k)
    {
        int meta = blockAccess.getBlockMetadata(i, j, k);
        int l = block.colorMultiplier(blockAccess, i, j, k);
        float f = (float)(l >> 16 & 0xff) / 255F;
        float f1 = (float)(l >> 8 & 0xff) / 255F;
        float f2 = (float)(l & 0xff) / 255F;
        if(EntityRenderer.field_28135_a)
        {
            float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
            float f4 = (f * 30F + f1 * 70F) / 100F;
            float f5 = (f * 30F + f2 * 70F) / 100F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        Tessellator tessellator = Tessellator.instance;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f7 = f4 * f;
        float f8 = f4 * f1;
        float f9 = f4 * f2;
        float f10 = f3;
        float f13 = f3;
        float f16 = f3;
        if(block != Block.grass)
        {
            f10 *= f;
            f13 *= f1;
            f16 *= f2;
        }
        float f19 = block.getBlockBrightness(blockAccess, i, j, k);
        if(blockAccess.getBlockId(i, j-1, k) != block.blockID) {
            float f20 = block.getBlockBrightness(blockAccess, i, j - 1, k);
            tessellator.setColorRGBA_F(f10 * f20, f13 * f20, f16 * f20, 0.25F);
            renderBottomFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 0));
        }
        float f21 = block.getBlockBrightness(blockAccess, i, j + 1, k);
        if(block.maxY != 1.0D && !block.blockMaterial.getIsLiquid())
            f21 = f19;
        tessellator.setColorRGBA_F(f7 * f21, f8 * f21, f9 * f21, 0.25F);
        renderTopFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 1));

        Random generator = new Random(i+j+k);
        double x = (generator.nextDouble()*0.35)*-(generator.nextDouble()*0.35+0.35);
        double y = (generator.nextDouble()*0.35)*-(generator.nextDouble()*0.35+0.35);
        double z = (generator.nextDouble()*0.35)*-(generator.nextDouble()*0.35+0.35);
        int fancy = ModOptions.FANCY_TREES.getAsInt();
        if(fancy == 2)
            func_1245_b(block, meta, i+x, j+y, k+z, true);
        else if(fancy == 1) {
            func_1245_b(block, meta, i+x, j+y, k+z);
            renderTallGrass(block, i, x, y, z, false,0.15F, 1);
        }
        return true;
    }

    /*private boolean reRenderBlockShader(int x, int y, int z) {
        int blockId = blockAccess.getBlockId(x, y, z);
        Block block = blockId <= 0 ? null : Block.blocksList[blockId];
        if (block == null)
            return true;
        int l = block.colorMultiplier(blockAccess, x, y, z);
        float f = (float) (l >> 16 & 0xff) / 255F;
        float f1 = (float) (l >> 8 & 0xff) / 255F;
        float f2 = (float) (l & 0xff) / 255F;
        if (EntityRenderer.field_28135_a) {
            float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
            float f4 = (f * 30F + f1 * 70F) / 100F;
            float f5 = (f * 30F + f2 * 70F) / 100F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        if (Minecraft.isAmbientOcclusionEnabled())
            renderStandardBlockWithAmbientOcclusion(block, x, y, z, f, f1, f2);
        else
            renderStandardBlockWithColorMultiplier(block, x, y, z, f, f1, f2);
        //MMUtil.getMinecraftInstance().theWorld.notifyBlocksOfNeighborChange(x, y, z, blockId);
        return false;
    }

    private static List<ShaderedBlock> shaderedBlocks = new ArrayList<>();
    private void loopThroughCurrentShades() {
        if(!shaderedBlocks.isEmpty()) {
            List<Vec3D> tempList = new ArrayList<>(shaderedBlocks);
            shaderedBlocks.clear();
            for (Vec3D vec3D : tempList) {
                try {
                    int x = (int) vec3D.xCoord;
                    int y = (int) vec3D.yCoord;
                    int z = (int) vec3D.zCoord;
                    reRenderBlockShader(x, y, z);
                } catch (NullPointerException | IndexOutOfBoundsException ignore) { }
            }
        }
    }

    public void computeShaders() {
        if(MMUtil.getMinecraftInstance().thePlayer != null) {
            for(int i=0;i < shaderedBlocks.size();i++) {
                ShaderedBlock shaderedBlock = shaderedBlocks.get(i);
                Vec3D vec3D = shaderedBlock.getVec3D();
                Block block = shaderedBlock.getBlock();
                int x = (int)vec3D.xCoord;
                int y = (int)vec3D.yCoord;
                int z = (int)vec3D.zCoord;
                //((IWorld) MMUtil.getMinecraftInstance().theWorld).forceNotifyChange(x, y, z, block.blockID);
            }
            if(null == null)
                return;
            //loopThroughCurrentShades();
            EntityPlayerSP player = MMUtil.getMinecraftInstance().thePlayer;
            int pX = (int) player.posX;
            int pY = (int) player.posY;
            int pZ = (int) player.posZ;
            //Vec3D pos = player.getPosition(0.0F);
            reRenderBlockShader(pX, pY, pZ);
            for (int i = -25; i < 25; i++) {
                int x = pX+i;
                for (int o = -50; o < 50; o++) {
                    int y = pY+o;
                    for (int p = -25; p < 25; p++) {
                        int z = pZ+p;
                        reRenderBlockShader(x, y, z);
                    }
                }
            }
        }
    }*/

    float divide = 12;
    int shaderType = 2;
    /**
     * @author cutezyash
     * @reason Shaders
     */
    @Overwrite
    public boolean renderStandardBlockWithAmbientOcclusion(Block block, int x, int y, int z, float f, float f1, float f2)
    {

        enableAO = true;
        boolean flag = false;
        float f3 = lightValueOwn;
        float f10 = lightValueOwn;
        float f17 = lightValueOwn;
        float f24 = lightValueOwn;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        boolean flag5 = true;
        boolean flag6 = true;
        lightValueOwn = block.getBlockBrightness(blockAccess, x, y, z);
        aoLightValueXNeg = block.getBlockBrightness(blockAccess, x - 1, y, z);
        aoLightValueYNeg = block.getBlockBrightness(blockAccess, x, y - 1, z);
        aoLightValueZNeg = block.getBlockBrightness(blockAccess, x, y, z - 1);
        aoLightValueXPos = block.getBlockBrightness(blockAccess, x + 1, y, z);
        aoLightValueYPos = block.getBlockBrightness(blockAccess, x, y + 1, z);
        aoLightValueZPos = block.getBlockBrightness(blockAccess, x, y, z + 1);
        EntityPlayerSP player = MMUtil.getPlayer().getPlayerSP();
        Vec3D curVec = Vec3D.createVector(x, y+1, z);
        Vec3D pos = player.getPosition(1.0F);
        int newType = (int) (ModOptions.SHADERS.getAsFloat() * 4);
        if (newType != shaderType)
            shaderType = newType;
        if (pos.distanceTo(curVec) < 50 && shaderType != 0) {
            if (shaderType == 2) {
                if (x % 2 == 0 && z % 2 == 0)
                    divide = 7.0F;
                else if (x % 2 == 1 && z % 2 == 1)
                    divide = 7.0F;
                else
                    divide = 4.0F;
            } else if(shaderType == 4) {
                divide = aoLightValueYNeg+5*((aoLightValueXNeg+aoLightValueZNeg)+1);
            } else
                divide = 4.0F;
            curVec.yCoord = curVec.yCoord - 1;
        } else
            divide = 4.0F;
        field_22338_U = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y + 1, z)];
        field_22359_ac = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y - 1, z)];
        field_22334_Y = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y, z + 1)];
        field_22363_aa = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y, z - 1)];
        field_22337_V = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y + 1, z)];
        field_22357_ad = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y - 1, z)];
        field_22335_X = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y, z - 1)];
        field_22333_Z = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y, z + 1)];
        field_22336_W = Block.canBlockGrass[blockAccess.getBlockId(x, y + 1, z + 1)];
        field_22339_T = Block.canBlockGrass[blockAccess.getBlockId(x, y + 1, z - 1)];
        field_22355_ae = Block.canBlockGrass[blockAccess.getBlockId(x, y - 1, z + 1)];
        field_22361_ab = Block.canBlockGrass[blockAccess.getBlockId(x, y - 1, z - 1)];
        if(block.blockIndexInTexture == 3)
        {
            flag1 = flag3 = flag4 = flag5 = flag6 = false;
        }
        if(overrideBlockTexture >= 0)
        {
            flag1 = flag3 = flag4 = flag5 = flag6 = false;
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0))
        {
            float f4;
            float f11;
            float f18;
            float f25;
            if(field_22352_G > 0)
            {
                y--;
                field_22376_n = block.getBlockBrightness(blockAccess, x - 1, y, z);
                field_22374_p = block.getBlockBrightness(blockAccess, x, y, z - 1);
                field_22373_q = block.getBlockBrightness(blockAccess, x, y, z + 1);
                field_22371_s = block.getBlockBrightness(blockAccess, x + 1, y, z);
                if(field_22361_ab || field_22357_ad)
                {
                    field_22377_m = block.getBlockBrightness(blockAccess, x - 1, y, z - 1);
                } else
                {
                    field_22377_m = field_22376_n;
                }
                if(field_22355_ae || field_22357_ad)
                {
                    field_22375_o = block.getBlockBrightness(blockAccess, x - 1, y, z + 1);
                } else
                {
                    field_22375_o = field_22376_n;
                }
                if(field_22361_ab || field_22359_ac)
                {
                    field_22372_r = block.getBlockBrightness(blockAccess, x + 1, y, z - 1);
                } else
                {
                    field_22372_r = field_22371_s;
                }
                if(field_22355_ae || field_22359_ac)
                {
                    field_22370_t = block.getBlockBrightness(blockAccess, x + 1, y, z + 1);
                } else
                {
                    field_22370_t = field_22371_s;
                }
                y++;
                f4 = (field_22375_o + field_22376_n + field_22373_q + aoLightValueYNeg) / divide;
                f25 = (field_22373_q + aoLightValueYNeg + field_22370_t + field_22371_s) / divide;
                f18 = (aoLightValueYNeg + field_22374_p + field_22371_s + field_22372_r) / divide;
                f11 = (field_22376_n + field_22377_m + aoLightValueYNeg + field_22374_p) / divide;
            } else
            {
                f4 = f11 = f18 = f25 = aoLightValueYNeg;
            }
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag1 ? f : 1.0F) * 0.5F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag1 ? f1 : 1.0F) * 0.5F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag1 ? f2 : 1.0F) * 0.5F;
            colorRedTopLeft *= f4;
            colorGreenTopLeft *= f4;
            colorBlueTopLeft *= f4;
            colorRedBottomLeft *= f11;
            colorGreenBottomLeft *= f11;
            colorBlueBottomLeft *= f11;
            colorRedBottomRight *= f18;
            colorGreenBottomRight *= f18;
            colorBlueBottomRight *= f18;
            colorRedTopRight *= f25;
            colorGreenTopRight *= f25;
            colorBlueTopRight *= f25;
            renderBottomFace(block, x, y, z, block.getBlockTexture(blockAccess, x, y, z, 0));
            flag = true;
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1))
        {
            float f5;
            float f12;
            float f19;
            float f26;
            if(field_22352_G > 0)
            {
                y++;
                field_22368_v = block.getBlockBrightness(blockAccess, x - 1, y, z);
                field_22364_z = block.getBlockBrightness(blockAccess, x + 1, y, z);
                field_22366_x = block.getBlockBrightness(blockAccess, x, y, z - 1);
                field_22362_A = block.getBlockBrightness(blockAccess, x, y, z + 1);
                if(field_22339_T || field_22337_V)
                {
                    field_22369_u = block.getBlockBrightness(blockAccess, x - 1, y, z - 1);
                } else
                {
                    field_22369_u = field_22368_v;
                }
                if(field_22339_T || field_22338_U)
                {
                    field_22365_y = block.getBlockBrightness(blockAccess, x + 1, y, z - 1);
                } else
                {
                    field_22365_y = field_22364_z;
                }
                if(field_22336_W || field_22337_V)
                {
                    field_22367_w = block.getBlockBrightness(blockAccess, x - 1, y, z + 1);
                } else
                {
                    field_22367_w = field_22368_v;
                }
                if(field_22336_W || field_22338_U)
                {
                    field_22360_B = block.getBlockBrightness(blockAccess, x + 1, y, z + 1);
                } else
                {
                    field_22360_B = field_22364_z;
                }
                y--;
                f26 = (field_22367_w + field_22368_v + field_22362_A + aoLightValueYPos) / divide;
                f5 = (field_22362_A + aoLightValueYPos + field_22360_B + field_22364_z) / divide;
                f12 = (aoLightValueYPos + field_22366_x + field_22364_z + field_22365_y) / divide;
                f19 = (field_22368_v + field_22369_u + aoLightValueYPos + field_22366_x) / divide;
            } else
            {
                f5 = f12 = f19 = f26 = aoLightValueYPos;
            }
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = flag2 ? f : 1.0F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = flag2 ? f1 : 1.0F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = flag2 ? f2 : 1.0F;
            colorRedTopLeft *= f5;
            colorGreenTopLeft *= f5;
            colorBlueTopLeft *= f5;
            colorRedBottomLeft *= f12;
            colorGreenBottomLeft *= f12;
            colorBlueBottomLeft *= f12;
            colorRedBottomRight *= f19;
            colorGreenBottomRight *= f19;
            colorBlueBottomRight *= f19;
            colorRedTopRight *= f26;
            colorGreenTopRight *= f26;
            colorBlueTopRight *= f26;
            renderTopFace(block, x, y, z, block.getBlockTexture(blockAccess, x, y, z, 1));
            flag = true;
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2))
        {
            float f6;
            float f13;
            float f20;
            float f27;
            if(field_22352_G > 0)
            {
                z--;
                field_22358_C = block.getBlockBrightness(blockAccess, x - 1, y, z);
                field_22374_p = block.getBlockBrightness(blockAccess, x, y - 1, z);
                field_22366_x = block.getBlockBrightness(blockAccess, x, y + 1, z);
                field_22356_D = block.getBlockBrightness(blockAccess, x + 1, y, z);
                if(field_22335_X || field_22361_ab)
                {
                    field_22377_m = block.getBlockBrightness(blockAccess, x - 1, y - 1, z);
                } else
                {
                    field_22377_m = field_22358_C;
                }
                if(field_22335_X || field_22339_T)
                {
                    field_22369_u = block.getBlockBrightness(blockAccess, x - 1, y + 1, z);
                } else
                {
                    field_22369_u = field_22358_C;
                }
                if(field_22363_aa || field_22361_ab)
                {
                    field_22372_r = block.getBlockBrightness(blockAccess, x + 1, y - 1, z);
                } else
                {
                    field_22372_r = field_22356_D;
                }
                if(field_22363_aa || field_22339_T)
                {
                    field_22365_y = block.getBlockBrightness(blockAccess, x + 1, y + 1, z);
                } else
                {
                    field_22365_y = field_22356_D;
                }
                z++;
                f6 = (field_22358_C + field_22369_u + aoLightValueZNeg + field_22366_x) / divide;
                f13 = (aoLightValueZNeg + field_22366_x + field_22356_D + field_22365_y) / divide;
                f20 = (field_22374_p + aoLightValueZNeg + field_22372_r + field_22356_D) / divide;
                f27 = (field_22377_m + field_22358_C + field_22374_p + aoLightValueZNeg) / divide;
            } else
            {
                f6 = f13 = f20 = f27 = aoLightValueZNeg;
            }
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag3 ? f : 1.0F) * 0.8F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag3 ? f1 : 1.0F) * 0.8F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag3 ? f2 : 1.0F) * 0.8F;
            colorRedTopLeft *= f6;
            colorGreenTopLeft *= f6;
            colorBlueTopLeft *= f6;
            colorRedBottomLeft *= f13;
            colorGreenBottomLeft *= f13;
            colorBlueBottomLeft *= f13;
            colorRedBottomRight *= f20;
            colorGreenBottomRight *= f20;
            colorBlueBottomRight *= f20;
            colorRedTopRight *= f27;
            colorGreenTopRight *= f27;
            colorBlueTopRight *= f27;
            int l = block.getBlockTexture(blockAccess, x, y, z, 2);
            renderEastFace(block, x, y, z, l);
            if(fancyGrass && l == 3 && overrideBlockTexture < 0)
            {
                colorRedTopLeft *= f;
                colorRedBottomLeft *= f;
                colorRedBottomRight *= f;
                colorRedTopRight *= f;
                colorGreenTopLeft *= f1;
                colorGreenBottomLeft *= f1;
                colorGreenBottomRight *= f1;
                colorGreenTopRight *= f1;
                colorBlueTopLeft *= f2;
                colorBlueBottomLeft *= f2;
                colorBlueBottomRight *= f2;
                colorBlueTopRight *= f2;
                renderEastFace(block, x, y, z, 38);
            }
            flag = true;
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3))
        {
            float f7;
            float f14;
            float f21;
            float f28;
            if(field_22352_G > 0)
            {
                z++;
                field_22354_E = block.getBlockBrightness(blockAccess, x - 1, y, z);
                field_22353_F = block.getBlockBrightness(blockAccess, x + 1, y, z);
                field_22373_q = block.getBlockBrightness(blockAccess, x, y - 1, z);
                field_22362_A = block.getBlockBrightness(blockAccess, x, y + 1, z);
                if(field_22333_Z || field_22355_ae)
                {
                    field_22375_o = block.getBlockBrightness(blockAccess, x - 1, y - 1, z);
                } else
                {
                    field_22375_o = field_22354_E;
                }
                if(field_22333_Z || field_22336_W)
                {
                    field_22367_w = block.getBlockBrightness(blockAccess, x - 1, y + 1, z);
                } else
                {
                    field_22367_w = field_22354_E;
                }
                if(field_22334_Y || field_22355_ae)
                {
                    field_22370_t = block.getBlockBrightness(blockAccess, x + 1, y - 1, z);
                } else
                {
                    field_22370_t = field_22353_F;
                }
                if(field_22334_Y || field_22336_W)
                {
                    field_22360_B = block.getBlockBrightness(blockAccess, x + 1, y + 1, z);
                } else
                {
                    field_22360_B = field_22353_F;
                }
                z--;
                f7 = (field_22354_E + field_22367_w + aoLightValueZPos + field_22362_A) / divide;
                f28 = (aoLightValueZPos + field_22362_A + field_22353_F + field_22360_B) / divide;
                f21 = (field_22373_q + aoLightValueZPos + field_22370_t + field_22353_F) / divide;
                f14 = (field_22375_o + field_22354_E + field_22373_q + aoLightValueZPos) / divide;
            } else
            {
                f7 = f14 = f21 = f28 = aoLightValueZPos;
            }
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag4 ? f : 1.0F) * 0.8F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag4 ? f1 : 1.0F) * 0.8F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag4 ? f2 : 1.0F) * 0.8F;
            colorRedTopLeft *= f7;
            colorGreenTopLeft *= f7;
            colorBlueTopLeft *= f7;
            colorRedBottomLeft *= f14;
            colorGreenBottomLeft *= f14;
            colorBlueBottomLeft *= f14;
            colorRedBottomRight *= f21;
            colorGreenBottomRight *= f21;
            colorBlueBottomRight *= f21;
            colorRedTopRight *= f28;
            colorGreenTopRight *= f28;
            colorBlueTopRight *= f28;
            int i1 = block.getBlockTexture(blockAccess, x, y, z, 3);
            renderWestFace(block, x, y, z, block.getBlockTexture(blockAccess, x, y, z, 3));
            if(fancyGrass && i1 == 3 && overrideBlockTexture < 0)
            {
                colorRedTopLeft *= f;
                colorRedBottomLeft *= f;
                colorRedBottomRight *= f;
                colorRedTopRight *= f;
                colorGreenTopLeft *= f1;
                colorGreenBottomLeft *= f1;
                colorGreenBottomRight *= f1;
                colorGreenTopRight *= f1;
                colorBlueTopLeft *= f2;
                colorBlueBottomLeft *= f2;
                colorBlueBottomRight *= f2;
                colorBlueTopRight *= f2;
                renderWestFace(block, x, y, z, 38);
            }
            flag = true;
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4))
        {
            float f8;
            float f15;
            float f22;
            float f29;
            if(field_22352_G > 0)
            {
                x--;
                field_22376_n = block.getBlockBrightness(blockAccess, x, y - 1, z);
                field_22358_C = block.getBlockBrightness(blockAccess, x, y, z - 1);
                field_22354_E = block.getBlockBrightness(blockAccess, x, y, z + 1);
                field_22368_v = block.getBlockBrightness(blockAccess, x, y + 1, z);
                if(field_22335_X || field_22357_ad)
                {
                    field_22377_m = block.getBlockBrightness(blockAccess, x, y - 1, z - 1);
                } else
                {
                    field_22377_m = field_22358_C;
                }
                if(field_22333_Z || field_22357_ad)
                {
                    field_22375_o = block.getBlockBrightness(blockAccess, x, y - 1, z + 1);
                } else
                {
                    field_22375_o = field_22354_E;
                }
                if(field_22335_X || field_22337_V)
                {
                    field_22369_u = block.getBlockBrightness(blockAccess, x, y + 1, z - 1);
                } else
                {
                    field_22369_u = field_22358_C;
                }
                if(field_22333_Z || field_22337_V)
                {
                    field_22367_w = block.getBlockBrightness(blockAccess, x, y + 1, z + 1);
                } else
                {
                    field_22367_w = field_22354_E;
                }
                x++;
                f29 = (field_22376_n + field_22375_o + aoLightValueXNeg + field_22354_E) / divide;
                f8 = (aoLightValueXNeg + field_22354_E + field_22368_v + field_22367_w) / divide;
                f15 = (field_22358_C + aoLightValueXNeg + field_22369_u + field_22368_v) / divide;
                f22 = (field_22377_m + field_22376_n + field_22358_C + aoLightValueXNeg) / divide;
            } else
            {
                f8 = f15 = f22 = f29 = aoLightValueXNeg;
            }
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag5 ? f : 1.0F) * 0.6F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag5 ? f1 : 1.0F) * 0.6F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag5 ? f2 : 1.0F) * 0.6F;
            colorRedTopLeft *= f8;
            colorGreenTopLeft *= f8;
            colorBlueTopLeft *= f8;
            colorRedBottomLeft *= f15;
            colorGreenBottomLeft *= f15;
            colorBlueBottomLeft *= f15;
            colorRedBottomRight *= f22;
            colorGreenBottomRight *= f22;
            colorBlueBottomRight *= f22;
            colorRedTopRight *= f29;
            colorGreenTopRight *= f29;
            colorBlueTopRight *= f29;
            int j1 = block.getBlockTexture(blockAccess, x, y, z, 4);
            renderNorthFace(block, x, y, z, j1);
            if(fancyGrass && j1 == 3 && overrideBlockTexture < 0)
            {
                colorRedTopLeft *= f;
                colorRedBottomLeft *= f;
                colorRedBottomRight *= f;
                colorRedTopRight *= f;
                colorGreenTopLeft *= f1;
                colorGreenBottomLeft *= f1;
                colorGreenBottomRight *= f1;
                colorGreenTopRight *= f1;
                colorBlueTopLeft *= f2;
                colorBlueBottomLeft *= f2;
                colorBlueBottomRight *= f2;
                colorBlueTopRight *= f2;
                renderNorthFace(block, x, y, z, 38);
            }
            flag = true;
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5))
        {
            float f9;
            float f16;
            float f23;
            float f30;
            if(field_22352_G > 0)
            {
                x++;
                field_22371_s = block.getBlockBrightness(blockAccess, x, y - 1, z);
                field_22356_D = block.getBlockBrightness(blockAccess, x, y, z - 1);
                field_22353_F = block.getBlockBrightness(blockAccess, x, y, z + 1);
                field_22364_z = block.getBlockBrightness(blockAccess, x, y + 1, z);
                if(field_22359_ac || field_22363_aa)
                {
                    field_22372_r = block.getBlockBrightness(blockAccess, x, y - 1, z - 1);
                } else
                {
                    field_22372_r = field_22356_D;
                }
                if(field_22359_ac || field_22334_Y)
                {
                    field_22370_t = block.getBlockBrightness(blockAccess, x, y - 1, z + 1);
                } else
                {
                    field_22370_t = field_22353_F;
                }
                if(field_22338_U || field_22363_aa)
                {
                    field_22365_y = block.getBlockBrightness(blockAccess, x, y + 1, z - 1);
                } else
                {
                    field_22365_y = field_22356_D;
                }
                if(field_22338_U || field_22334_Y)
                {
                    field_22360_B = block.getBlockBrightness(blockAccess, x, y + 1, z + 1);
                } else
                {
                    field_22360_B = field_22353_F;
                }
                x--;
                f9 = (field_22371_s + field_22370_t + aoLightValueXPos + field_22353_F) / divide;
                f30 = (aoLightValueXPos + field_22353_F + field_22364_z + field_22360_B) / divide;
                f23 = (field_22356_D + aoLightValueXPos + field_22365_y + field_22364_z) / divide;
                f16 = (field_22372_r + field_22371_s + field_22356_D + aoLightValueXPos) / divide;
            } else
            {
                f9 = f16 = f23 = f30 = aoLightValueXPos;
            }
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag6 ? f : 1.0F) * 0.6F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag6 ? f1 : 1.0F) * 0.6F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag6 ? f2 : 1.0F) * 0.6F;
            colorRedTopLeft *= f9;
            colorGreenTopLeft *= f9;
            colorBlueTopLeft *= f9;
            colorRedBottomLeft *= f16;
            colorGreenBottomLeft *= f16;
            colorBlueBottomLeft *= f16;
            colorRedBottomRight *= f23;
            colorGreenBottomRight *= f23;
            colorBlueBottomRight *= f23;
            colorRedTopRight *= f30;
            colorGreenTopRight *= f30;
            colorBlueTopRight *= f30;
            int k1 = block.getBlockTexture(blockAccess, x, y, z, 5);
            renderSouthFace(block, x, y, z, k1);
            if(fancyGrass && k1 == 3 && overrideBlockTexture < 0)
            {
                colorRedTopLeft *= f;
                colorRedBottomLeft *= f;
                colorRedBottomRight *= f;
                colorRedTopRight *= f;
                colorGreenTopLeft *= f1;
                colorGreenBottomLeft *= f1;
                colorGreenBottomRight *= f1;
                colorGreenTopRight *= f1;
                colorBlueTopLeft *= f2;
                colorBlueBottomLeft *= f2;
                colorBlueBottomRight *= f2;
                colorBlueTopRight *= f2;
                renderSouthFace(block, x, y, z, 38);
            }
            flag = true;
        }
        enableAO = false;
        return flag;
    }
}
