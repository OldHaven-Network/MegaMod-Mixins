package net.oldhaven.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.Session;
import net.oldhaven.customs.CustomKeybinds;
import net.oldhaven.MegaMod;
import net.oldhaven.SkinFix;
import net.oldhaven.gui.changelog.ChangeLog;
import net.oldhaven.javascript.JSEngine;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow public FontRenderer fontRenderer;
	@Shadow public GameSettings gameSettings;
	@Shadow public Session session;
	private CustomKeybinds customKeybinds;

	@Inject(method = "startGame", at = @At("HEAD"))
	private void onRun(CallbackInfo ci) {
		new JSEngine();
		new MegaMod();
		SkinFix.tryConnection();
		new ChangeLog();
	}

	@Redirect(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V"))
	private void displayCreate() {
		try {
			PixelFormat pixelformat = new PixelFormat();
			pixelformat = pixelformat.withDepthBits(24);
			Display.create(pixelformat);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;handleKeyPress(IZ)V", shift = At.Shift.BEFORE))
	private void onKeyPressHold(CallbackInfo ci) {
		Map<String, CustomKeybinds.SavedKey> keys = MegaMod.getInstance().getCustomKeybinds().getSavedKeys();
		for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keys.entrySet()) {
			if(entry.getValue().getKeyType() != 1)
				continue;
			boolean b = Keyboard.isKeyDown(entry.getValue().getKey());
			MegaMod.getInstance().getCustomKeybinds().onKey(entry.getKey(), b);
		}
	}

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 2, shift = At.Shift.BEFORE))
	private void onKeyPress(CallbackInfo ci) {
		if(Keyboard.getEventKey() == gameSettings.keyBindJump.keyCode)
			MegaMod.getInstance().getCustomKeybinds().onKey("Jump", true);
		Map<String, CustomKeybinds.SavedKey> keys = MegaMod.getInstance().getCustomKeybinds().getSavedKeys();
		for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keys.entrySet()) {
			if(entry.getValue().getKeyType() != 0)
				continue;
			if(Keyboard.getEventKey() == entry.getValue().getKey()) {
				MegaMod.getInstance().getCustomKeybinds().onKey(entry.getKey(), true);
			}
		}
	}
}
