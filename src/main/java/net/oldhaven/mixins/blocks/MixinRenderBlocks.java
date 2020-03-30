package net.oldhaven.mixins.blocks;

import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(RenderBlocks.class)
public abstract class MixinRenderBlocks {
    @Shadow private int overrideBlockTexture;
    @Shadow private IBlockAccess blockAccess;
    @Shadow private boolean renderAllFaces;
    @Shadow public abstract void renderBottomFace(Block block, double v, double v1, double v2, int i);
    @Shadow public abstract void renderTopFace(Block block, double v, double v1, double v2, int i);

    private boolean[] lastRendering;
    @Inject(method = "<init>(Lnet/minecraft/src/IBlockAccess;)V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        lastRendering = new boolean[]{false};
    }

    @Inject(method = "renderBlockByRenderType", at = @At("HEAD"), cancellable = true)
    public void renderBlockByRenderType(Block var1, int var2, int var3, int var4, CallbackInfoReturnable<Boolean> ci) {
        boolean fancyTress = (MegaMod.getInstance().getCustomGameSettings().getOptionI("Enable Fancy Trees") == 1);
        if(fancyTress && var1.blockID == 18)
                ci.setReturnValue(renderBlockLeaves(var1, var2, var3, var4));
    }

    public void func_1245_b(Block block, int i, double d, double d1, double d2, boolean random)
    {
        Tessellator tessellator = Tessellator.instance;
        int j = block.getBlockTextureFromSideAndMetadata(0, i);
        if(overrideBlockTexture >= 0)
        {
            j = overrideBlockTexture;
        }

        double x = 0;
        double y = 0;
        double z = 0;
        if(random) {
            double times = 0.15F;
            Random ran = new Random((long) (d + d1 + d2));
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
            if (renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0)) {
                float f20 = block.getBlockBrightness(blockAccess, i, j - 1, k);
                tessellator.setColorOpaque_F(f10 * f20, f13 * f20, f16 * f20);
                renderBottomFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 0));
            }
        }
        if(renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1))
        {

            float f21 = block.getBlockBrightness(blockAccess, i, j + 1, k);
            if(block.maxY != 1.0D && !block.blockMaterial.getIsLiquid())
                f21 = f19;
            tessellator.setColorOpaque_F(f7 * f21, f8 * f21, f9 * f21);
            renderTopFace(block, i, j, k, block.getBlockTexture(blockAccess, i, j, k, 1));
        }

        Random generator = new Random(i+j+k);
        double x = (generator.nextDouble()*0.35)*-(generator.nextDouble()*0.35+0.35);
        double y = (generator.nextDouble()*0.35)*-(generator.nextDouble()*0.35+0.35);
        double z = (generator.nextDouble()*0.35)*-(generator.nextDouble()*0.35+0.35);
        func_1245_b(block, blockAccess.getBlockMetadata(i, j, k), i+x, j+y, k+z, true);
        return true;
    }
}
