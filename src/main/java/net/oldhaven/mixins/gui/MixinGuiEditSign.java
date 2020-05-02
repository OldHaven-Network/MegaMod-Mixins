package net.oldhaven.mixins.gui;

import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.TileEntitySign;
import net.oldhaven.MegaMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEditSign.class)
public class MixinGuiEditSign extends GuiScreen {
    @Shadow private TileEntitySign entitySign;
    @Shadow private int editLine;
    @Final @Shadow private static String allowedCharacters;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        MegaMod.getInstance().signCursorLoc = 0;
    }

    @Inject(method = "onGuiClosed", at = @At("RETURN"))
    private void onGuiClosed(CallbackInfo ci) {
        MegaMod.getInstance().signCursorLoc = 0;
    }

    @Inject(method = "keyTyped", at = @At("HEAD"))
    private void keyTyped1(char c, int i, CallbackInfo ci) {
        int signCursorLoc = MegaMod.getInstance().signCursorLoc;
        if(editLine > 3)
            editLine = 3;
        if(editLine < 0)
            editLine = 0;
        if(i == 200) { /* UP key */
            if(editLine > 0)
                signCursorLoc = entitySign.signText[editLine-1].length();
        } else if(i == 208 || i == 28) { /* DOWN key */
            if(editLine < entitySign.signText.length-1)
                signCursorLoc = entitySign.signText[editLine+1].length();
            else
                signCursorLoc = 0;
        } else if(i == 203 && signCursorLoc > 0) { /* LEFT key */
            signCursorLoc -= 1;
        } else if(i == 205 && signCursorLoc < entitySign.signText[editLine].length()) { /* RIGHT key */
            signCursorLoc += 1;
        } else if(i == 199) { /* HOME key */
            signCursorLoc = 0;
        } else if(i == 207) { /* END key */
            signCursorLoc = entitySign.signText[editLine].length();
        }
        MegaMod.getInstance().signCursorLoc = signCursorLoc;
    }

    private int substringTest(String msg, int cursorLoc) {
        if(cursorLoc > msg.length())
            cursorLoc = msg.length();
        return cursorLoc;
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void drawScreen(CallbackInfo ci) {
        MegaMod.getInstance().signCursorLoc = substringTest(this.entitySign.signText[this.editLine], MegaMod.getInstance().signCursorLoc);
        this.drawCenteredString(this.fontRenderer, "Cursor: " + MegaMod.getInstance().signCursorLoc, this.width / 2, 18, 0xffffff);
        this.drawCenteredString(this.fontRenderer, "Length: " + entitySign.signText[editLine].length(), this.width / 2, 28, 0xffffff);
    }

    @Redirect(
            method = "keyTyped",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(II)Ljava/lang/String;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", ordinal = 0),
                             to = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 0)))
    private String redirect1(String s, int a, int b, char c, int i) {
        MegaMod.getInstance().signCursorLoc = substringTest(this.entitySign.signText[this.editLine], MegaMod.getInstance().signCursorLoc);
        int cursorLoc = MegaMod.getInstance().signCursorLoc;
        String newS = this.entitySign.signText[this.editLine].substring(0, cursorLoc-1) + this.entitySign.signText[this.editLine].substring(cursorLoc);
        MegaMod.getInstance().signCursorLoc-=1;
        return newS;
    }

    @Redirect(
            method = "keyTyped",
            at = @At(value = "INVOKE",target = "Ljava/lang/StringBuilder;toString()Ljava/lang/String;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 0)))
    private String redirect3(StringBuilder builder, char c, int i) {
        MegaMod.getInstance().signCursorLoc= substringTest(this.entitySign.signText[this.editLine], MegaMod.getInstance().signCursorLoc);
        int cursorLoc = MegaMod.getInstance().signCursorLoc;
        String[] var10002 = this.entitySign.signText;
        int var10004 = this.editLine;
        MegaMod.getInstance().signCursorLoc+=1;
        return var10002[var10004].substring(0, cursorLoc) + c + var10002[var10004].substring(cursorLoc);
    }
}
