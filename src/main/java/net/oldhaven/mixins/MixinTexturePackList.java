package net.oldhaven.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TexturePackList.class)
public class MixinTexturePackList {
    @Shadow public TexturePackBase selectedTexturePack;
    @Shadow private String currentTexturePack;
    @Shadow private Minecraft mc;

    /**
     * @author Logan
     * @reason Fix texture pack switching
     */
    @Overwrite
    public boolean setTexturePack(TexturePackBase texturepackbase)
    {
        if(texturepackbase == selectedTexturePack)
        {
            return false;
        } else
        {
            selectedTexturePack.closeTexturePackFile();
            currentTexturePack = texturepackbase.texturePackFileName;
            selectedTexturePack = texturepackbase;
            mc.gameSettings.skin = currentTexturePack;
            mc.gameSettings.saveOptions();
            selectedTexturePack.func_6482_a();
            //mc.fontRenderer = new FontRenderer(mc.gameSettings, texturepackbase, mc.renderEngine);
            mc.renderEngine.refreshTextures();
            mc.renderGlobal.loadRenderers();
            ColorizerWater.func_28182_a(mc.renderEngine.func_28149_a("/misc/watercolor.png"));
            ColorizerGrass.func_28181_a(mc.renderEngine.func_28149_a("/misc/grasscolor.png"));
            ColorizerFoliage.func_28152_a(mc.renderEngine.func_28149_a("/misc/foliagecolor.png"));
            return true;
        }
    }
}
