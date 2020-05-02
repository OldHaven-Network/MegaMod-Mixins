package net.oldhaven.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.options.CustomKeybinds;
import net.oldhaven.MegaMod;
import net.oldhaven.SkinFix;
import net.oldhaven.customs.shaders.Shader;
import net.oldhaven.gui.changelog.ChangeLog;
import net.oldhaven.javascript.JSEngine;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow public FontRenderer fontRenderer;
	@Shadow public GameSettings gameSettings;
	@Shadow public Session session;
	@Shadow public EntityPlayerSP thePlayer;
	@Shadow private static Minecraft theMinecraft;
	private CustomKeybinds customKeybinds;

	@Inject(method = "startGame", at = @At("HEAD"))
	private void onRun(CallbackInfo ci) {
		new JSEngine();
		new MegaMod();
		SkinFix.tryConnection();
		new ChangeLog();
	}

	@Redirect(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V"), require = 0)
	private void displayCreate() {
		try {
			PixelFormat pixelformat = new PixelFormat();
			pixelformat = pixelformat.withDepthBits(24);
			Display.create(pixelformat);
			Shader.x = theMinecraft;
			Shader.setUpBuffers();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;handleKeyPress(IZ)V", shift = At.Shift.BEFORE))
	private void onKeyPressHold(CallbackInfo ci) {
		Map<String, CustomKeybinds.SavedKey> keys = MegaMod.getCustomKeybinds().getSavedKeys();
		for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keys.entrySet()) {
			if(entry.getValue().getKeyType() != 1)
				continue;
			boolean b = Keyboard.isKeyDown(entry.getValue().getKey());
			MegaMod.getCustomKeybinds().onKey(entry.getKey(), b);
		}
	}

	@Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;thirdPersonView:Z", opcode = Opcodes.PUTFIELD))
	private void constant63(GameSettings settings, boolean value) {
		int third = MegaMod.thirdPersonView;
		third++;
		if(third > 2)
			third = 0;
		MegaMod.thirdPersonView = third;
		gameSettings.thirdPersonView = third != 0 && (third == 1 ? true : true);
	}

	@Inject(method = "run", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;thirdPersonView:Z", shift = At.Shift.AFTER))
	private void redirectThird(CallbackInfo ci) {
		MegaMod.thirdPersonView = 0;
	}

	@Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isMultiplayerWorld()Z"))
	private boolean isMultiplayer(Minecraft minecraft) {
		return true;
	}

	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 2, shift = At.Shift.BEFORE))
	private void onKeyPress(CallbackInfo ci) {
		if(Keyboard.getEventKey() == 63) {
			int third = MegaMod.thirdPersonView+1;
			if(third > 2)
				third = 0;
			MegaMod.thirdPersonView = third;
		}
		if(Keyboard.getEventKey() == gameSettings.keyBindJump.keyCode)
			MegaMod.getCustomKeybinds().onKey("Jump", true);
		Map<String, CustomKeybinds.SavedKey> keys = MegaMod.getCustomKeybinds().getSavedKeys();
		for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keys.entrySet()) {
			if(entry.getValue().getKeyType() != 0)
				continue;
			if(Keyboard.getEventKey() == entry.getValue().getKey()) {
				MegaMod.getCustomKeybinds().onKey(entry.getKey(), true);
			}
		}
	}
}
