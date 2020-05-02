package net.oldhaven.mixins.shader.glsl;

import net.minecraft.src.*;
import net.oldhaven.customs.shaders.ITessellator;
import net.oldhaven.customs.shaders.Shader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer_EntityAttrib {
    @Shadow
    public World worldObj;
    @Redirect(method = "updateRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/src/Block;III)Z", opcode = Opcodes.INVOKEVIRTUAL))
    private boolean updateRenderer1(RenderBlocks renderBlocks, Block block, int i, int i1, int i2) {
        if (Shader.entityAttrib >= 0)
            Shader.setEntity(block.blockID, this.worldObj.getBlockLightValue(i, i1, i2), Block.lightValue[block.blockID]);
        return renderBlocks.renderBlockByRenderType(block, i, i1, i2);
    }

    @Inject(method = "updateRenderer", at = @At("RETURN"))
    private void updateRenderer2(CallbackInfo ci) {
        if (Shader.entityAttrib >= 0)
            ((ITessellator)Tessellator.instance).setEntity(-1, 0, 0);
    }
}
