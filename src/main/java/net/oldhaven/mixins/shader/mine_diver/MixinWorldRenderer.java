package net.oldhaven.mixins.shader.mine_diver;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.WorldRenderer;
import net.mine_diver.glsl.Shaders;
import net.mine_diver.glsl.util.TessellatorShaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Redirect(method = "updateRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/src/Block;III)Z"))
    private boolean onRenderBlockByRenderType(RenderBlocks renderBlocks, Block var24, int var17, int var15, int var16) {
        if (Shaders.entityAttrib >= 0)
            ((TessellatorShaders) Tessellator.instance).setEntity(var24.blockID);
        return renderBlocks.renderBlockByRenderType(var24, var17, var15, var16);
    }

    @Inject(method = "updateRenderer", at = @At(value = "RETURN"))
    private void onUpdateRenderer(CallbackInfo ci) {
        if (Shaders.entityAttrib >= 0)
            ((TessellatorShaders) Tessellator.instance).setEntity(-1);
    }
}
