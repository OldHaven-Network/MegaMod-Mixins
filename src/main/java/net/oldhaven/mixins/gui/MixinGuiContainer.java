package net.oldhaven.mixins.gui;

import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer extends GuiScreen {
    @Shadow protected int xSize;
    @Shadow protected int ySize;

    private int var1;
    private int var2;

    /**
     * prep for Modern Tooltips setting
     * @param i x on screen
     * @param i1 z on screen
     * @param v tickback
     * @param ci callback
     */
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreen(int i, int i1, float v, CallbackInfo ci) {
        this.var1 = i;
        this.var2 = i1;
    }

    /**
     * MegaMod Modern Tooltips trigger
     * @param itemStack stack currently hovering over
     * @return original str if tooltips are off
     */
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ItemStack;getItemName()Ljava/lang/String;"))
    private String drawGradientRect(ItemStack itemStack) {
        if(ModOptions.MODERN_TOOLTIPS.getAsInt() != 1)
            return itemStack.getItemName();
        String var13 = ("" + StringTranslate.getInstance().translateNamedKey(itemStack.getItemName())).trim();
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        if(var13.length() > 0) {
            int var9 = var1 - var4 + 12;
            int var10 = var2 - var5 - 12;
            int var11 = this.fontRenderer.getStringWidth(var13);
            Item item = itemStack.getItem();
            if(itemStack.getItem().isDamagable()) {
                int maxDur = itemStack.getMaxDamage();
                int dur = maxDur - itemStack.getItemDamage();
                String damMsg = "+0 Damage";
                if (item instanceof ItemTool) {
                    ItemTool tool = ((ItemTool) item);
                    String vs = ((int)tool.getStrVsBlock(itemStack, Block.stone)) + "";
                    damMsg = "+" + vs + " Block Damage";
                } else if (item instanceof ItemSword) {
                    ItemSword tool = ((ItemSword) item);
                    String vs = tool.getDamageVsEntity(mc.thePlayer) + "";
                    damMsg = "+" + vs + " Entity Damage";
                }
                String durMsg = "Durability: " + dur + "/" + maxDur;
                int damLen = this.fontRenderer.getStringWidth(damMsg);
                int durLen = this.fontRenderer.getStringWidth(durMsg);
                if (damLen > var11)
                    var11 = durLen;
                if (durLen > var11)
                    var11 = durLen;
                this.drawGradientRect(var9 - 3, var10 - 3, var9 + var11 + 3, var10 + 8 + 3 + 12 + 3 + 8 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawStringWithShadow(var13, var9, var10, -1);
                this.fontRenderer.drawStringWithShadow(damMsg, var9, var10 + 12 + 3, 0x5555FF);
                this.fontRenderer.drawStringWithShadow(durMsg, var9, var10 + 12 + 3 + 8 + 3, 0xAAAAAA);
            } else if(item instanceof ItemFood) {
                ItemFood tool = ((ItemFood) item);
                String vs = tool.getHealAmount()+"";
                String healMsg = "+" + vs + " Heal";
                int healLen = this.fontRenderer.getStringWidth(healMsg);
                if(healLen > var11)
                    var11 = healLen;
                this.drawGradientRect(var9 - 3, var10 - 3, var9 + var11 + 3, var10 + 8 + 3 + 12 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawStringWithShadow(var13, var9, var10, -1);
                this.fontRenderer.drawStringWithShadow(healMsg, var9, var10 + 12 + 3, 0x5555FF);
            } else {
                this.drawGradientRect(var9 - 3, var10 - 3, var9 + var11 + 3, var10 + 8 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawStringWithShadow(var13, var9, var10, -1);
            }
        }
        return "";
    }
}
