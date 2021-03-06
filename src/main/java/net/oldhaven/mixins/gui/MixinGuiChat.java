package net.oldhaven.mixins.gui;

import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiScreen;
import net.oldhaven.customs.SinglePlayerCommands;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.packets.util.PacketList;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.SkinFix;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {
    @Shadow protected String message;
    @Shadow private int updateCounter;
    private static LinkedList<String> enteredChats = new LinkedList<>();
    private int currentUp = -1;
    private boolean isTyping = false;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        MMUtil.chatCursorLoc = 0;
    }

    @Inject(method = "onGuiClosed", at = @At("RETURN"))
    private void onGuiClosed(CallbackInfo ci) {
        isTyping = false;
        MMUtil.chatCursorLoc = 0;
        PacketList.PLAYERTYPING.send("STOPTYPING");
    }

    @Redirect(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;sendChatMessage(Ljava/lang/String;)V"))
    private void sendMessage(EntityPlayerSP entityPlayerSP, String s) {
        if(s.equalsIgnoreCase("/unloadskins")) {
            SkinFix.unload();
            return;
        }
        entityPlayerSP.sendChatMessage(s);
        if(ModOptions.SP_CHEATS.getAsBool() && !mc.isMultiplayerWorld()) {
            if(s.startsWith("/")) {
                String[] split = s.split(" ");
                String cmd = split[0].substring(1).toLowerCase();
                if(SinglePlayerCommands.runnableMap.containsKey(cmd)) {
                    String[] args = new String[split.length-1];
                    System.arraycopy(split, 1, args, 0, split.length - 1);
                    SinglePlayerCommands.runnableMap.get(cmd).run(args, entityPlayerSP);
                }
            }
        }
    }

    private void startTyping(Character c) {
        if(message.isEmpty() && c != null && c != '/'
        ||!message.isEmpty() && message.charAt(0) != '/') {
            if(!isTyping) {
                isTyping = true;
                PacketList.PLAYERTYPING.send("STARTTYPING");
            }
        }
    }

    @Inject(method = "keyTyped", at = @At("HEAD"))
    private void keyTyped(char c, int i, CallbackInfo ci) {
        int cursorLoc = MMUtil.chatCursorLoc;
        if(i == 203 && cursorLoc > 0) { /* LEFT key */
            cursorLoc -= 1;
        } else if(i == 205 && cursorLoc < this.message.length()) { /* RIGHT key */
            cursorLoc += 1;
        } else if(i == 200) {
            if(enteredChats.size() > 0 && (currentUp+1) < enteredChats.size()) {
                currentUp += 1;
                message = enteredChats.get(currentUp);
                cursorLoc = message.length();
                startTyping(null);
            }
        } else if(i == 208) {
            if((currentUp-1) <= -1) {
                message = "";
                cursorLoc = 0;
                currentUp = -1;
            } else {
                currentUp -= 1;
                message = enteredChats.get(currentUp);
                cursorLoc = message.length();
                startTyping(null);
            }
        } else if(i == 199 || i == 28) /* HOME key, ENTER key */
            cursorLoc = 0;
        else if(i == 207) /* END key */
            cursorLoc = message.length();
        else {
            startTyping(c);
        }
        MMUtil.chatCursorLoc = cursorLoc;
    }

    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;lineIsCommand(Ljava/lang/String;)Z", ordinal = 0))
    private void keyTyped2(char c, int i, CallbackInfo ci) {
        String s1 = this.message.trim();
        enteredChats.addFirst(s1);
        currentUp = 0;
    }

    private int substringTest(String msg, int cursorLoc) {
        if(cursorLoc > msg.length())
            cursorLoc = msg.length();
        MMUtil.chatCursorLoc = cursorLoc;
        return cursorLoc;
    }

    @Redirect(
            method = "keyTyped",
            at = @At(value = "FIELD", target = "Lnet/minecraft/src/GuiChat;message:Ljava/lang/String;", opcode = Opcodes.PUTFIELD),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", ordinal = 2),
                to = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 0)))
    private void redirect1(GuiChat guiChat, String value) {
        int cursorLoc = MMUtil.chatCursorLoc;
        if(cursorLoc-1 < 0)
            return;
        this.message = this.message.substring(0, cursorLoc-1) + this.message.substring(cursorLoc);
        MMUtil.chatCursorLoc = cursorLoc-1;
    }

    @Redirect(
            method = "keyTyped",
            at = @At(value = "FIELD",target = "Lnet/minecraft/src/GuiChat;message:Ljava/lang/String;", opcode = Opcodes.PUTFIELD),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 3)))
    private void redirect2(GuiChat guiChat, String value, char c, int i) {
        int cursorLoc = MMUtil.chatCursorLoc;
        this.message = this.message.substring(0, cursorLoc) + c + this.message.substring(cursorLoc);
        MMUtil.chatCursorLoc = cursorLoc+1;
    }

    /**
     * @author cutezyash
     * @reason Show cursor in chat line
     */
    @Overwrite
    public void drawScreen(int var1, int var2, float var3) {
        this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, -2147483648);
        MMUtil.chatCursorLoc = substringTest(this.message, MMUtil.chatCursorLoc);
        int cursor = MMUtil.chatCursorLoc;
        String msg = this.message;
        msg = msg.substring(0, cursor) + ((updateCounter / 6) % 2 != 0 ? "" : "|") + msg.substring(cursor);
        this.drawString(this.fontRenderer, "> " + msg, 4, this.height - 12, 14737632);
        super.drawScreen(var1, var2, var3);
    }
}
