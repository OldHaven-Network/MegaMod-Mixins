package net.oldhaven.mixins.entity;

import net.minecraft.src.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    /*private EntityLiving entityLiving;
    private ItemStack itemStack;
    @Inject(method = "renderItem", at = @At("HEAD"))
    private void renderItemStart(EntityLiving entityLiving, ItemStack itemStack, CallbackInfo ci) {
        this.entityLiving = entityLiving;
        this.itemStack = itemStack;
    }

    @Redirect(method = "renderItem", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 1))
    private void translateForAlex(float x, float y, float z) {
        System.out.println(entityLiving.getClass().getName());
        if(!(entityLiving instanceof EntityPlayer))
            return;
        System.out.println("render item");
        EntityPlayer player = (EntityPlayer) entityLiving;
        if(SkinFix.isSkinAlex(player.username))
            x += 5F;
        GL11.glTranslatef(x, y, z);
    }*/
}
