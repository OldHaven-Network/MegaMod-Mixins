package net.oldhaven.mixins;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.IFontRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements IFontRenderer {
    @Shadow public abstract void renderString(String s, int i, int i1, int i2, boolean b);
    private int[] charWidth = new int[256];
    public int fontTextureName = 0;
    private int fontDisplayLists;
    private IntBuffer buffer;

    @Inject(method = "<init>", at=@At("RETURN"))
    private void init(CallbackInfo ci) {
        //this.buffer = GLAllocation.createDirectIntBuffer(1024);
        //this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
        //MegaMod.getInstance().BeginThread();
        MegaMod.setFontRenderer(this);
    }

    public void renderString(String var1, int var2, int var3, float[] var4, boolean var5) {
        if(var4 == null)
            return;
        if (var1 != null) {
            int var6;

            GL11.glBindTexture(3553, this.fontTextureName);

            GL11.glColor4f(var4[0], var4[1], var4[2], var4[3]);
            this.buffer.clear();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)var2, (float)var3, 0.0F);

            for(var6 = 0; var6 < var1.length(); ++var6) {
                int var11;
                for(; var1.length() > var6 + 1 && var1.charAt(var6) == 167; var6 += 2) {
                    var11 = "0123456789abcdef".indexOf(var1.toLowerCase().charAt(var6 + 1));
                    if (var11 < 0 || var11 > 15) {
                        var11 = 15;
                    }

                    this.buffer.put(this.fontDisplayLists + 256 + var11 + (var5 ? 16 : 0));
                    if (this.buffer.remaining() == 0) {
                        this.buffer.flip();
                        GL11.glCallLists(this.buffer);
                        this.buffer.clear();
                    }
                }

                if (var6 < var1.length()) {
                    var11 = ChatAllowedCharacters.allowedCharacters.indexOf(var1.charAt(var6));
                    if (var11 >= 0) {
                        this.buffer.put(this.fontDisplayLists + var11 + 32);
                    }
                }

                if (this.buffer.remaining() == 0) {
                    this.buffer.flip();
                    GL11.glCallLists(this.buffer);
                    this.buffer.clear();
                }
            }

            this.buffer.flip();
            GL11.glCallLists(this.buffer);
            GL11.glPopMatrix();
        }
    }
}
