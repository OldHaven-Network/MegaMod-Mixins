package net.oldhaven.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.*;
import net.oldhaven.MMDebug;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.CustomKeybinds;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.SkinFix;
import net.oldhaven.gui.changelog.ChangeLog;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;

import static net.oldhaven.MMDebug.println;
import static net.oldhaven.MMDebug.printe;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Shadow public FontRenderer fontRenderer;
	@Shadow public GameSettings gameSettings;
	@Shadow public EntityPlayerSP thePlayer;
	@Shadow private static Minecraft theMinecraft;
	@Shadow public RenderGlobal renderGlobal;

	@Shadow public abstract void run();

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(Component component, Canvas canvas, MinecraftApplet minecraftApplet, int i, int i1, boolean b, CallbackInfo ci) {
		try {
			String env = System.getenv("MMDebug");
			if(!env.isEmpty()) {
				MMDebug.enabled = true;
				MMDebug.debugUserName = env;
				System.out.println("Debug mode is ENABLED");
			}
		} catch(Exception e) {
			e.printStackTrace();
			MMDebug.enabled = false;
		}
	}

	@Redirect(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Controllers;create()V"))
	private void createController() {}

	/**
	 * Before MC starts, initiate MegaMod
	 * @param ci callback, unused
	 */
	@Inject(method = "startGame", at = @At("HEAD"))
	private void onRun(CallbackInfo ci) {
		new MegaMod();
		SkinFix.tryConnection();
		new ChangeLog();
	}

	/**
	 * Indicates Minecraft window has fully started
	 * @param ci callback
	 */
	@Inject(method = "startGame", at = @At("RETURN"))
	private void onStarted(CallbackInfo ci) {
		if(MMDebug.enabled) {
			System.out.println("-----------------");
			System.out.println("!!MegaMod IS IN DEBUG MODE!!");
			System.out.println("THIS MODE IS ONLY FOR EDITING AND RESOLVING MegaMod ISSUES!");

			OperatingSystemMXBean op = ManagementFactory.getOperatingSystemMXBean();
			Runtime runtime = Runtime.getRuntime();
			printe();
			println("SYS Info (In-case of Faults): ");
			println(" - OS: " + op.getName() + ", " + op.getArch());
			println(" - Free Mem (Bytes): " + runtime.freeMemory());
			long maxMem = runtime.maxMemory();
			println(" - Max Mem (bytes): " + ((maxMem == Long.MAX_VALUE) ? "INFINITE" : maxMem));
			println(" - Mem For JVM (bytes): " + runtime.totalMemory());
			println(" - Available Cores: " + runtime.availableProcessors());
			println(" - GL Renderer: " + GL11.glGetString(GL11.GL_RENDERER));
			println(" - GL Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
			println(" - GL Version: " + GL11.glGetString(GL11.GL_VERSION));
			//println(" - GL EXT: " + GL11.glGetString(GL11.GL_EXTENSIONS));
			System.out.println("-----------------");
		}
		MegaMod.getInstance().onMinecraftStarted();
	}

	/**
	 * Overwrite display creation for Shaders
	 */
	@Redirect(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V"), require = 0)
	private void displayCreate() {
		try {
			PixelFormat pixelformat = new PixelFormat();
			pixelformat = pixelformat.withDepthBits(24);
			Display.create(pixelformat);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reloads renderer if ctrl+fogKey is pressed, turns
	 * down fog if ctrl is not pressed as normal.
	 * @author cutezyash
	 * @param settings gameSettings
	 * @param enumOptions option
	 * @param i integer
	 */
	@Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GameSettings;setOptionValue(Lnet/minecraft/src/EnumOptions;I)V"))
	private void setRenderDistance(GameSettings settings, EnumOptions enumOptions, int i) {
		if(renderGlobal != null) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				renderGlobal.loadRenderers();
			} else {
				settings.setOptionValue(enumOptions, i);
			}
		}
	}

	/**
	 * MegaMod custom keybinds trigger
	 * @param ci callback
	 */
	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;handleKeyPress(IZ)V", shift = At.Shift.BEFORE))
	private void onKeyPressHold(CallbackInfo ci) {
		Map<String, CustomKeybinds.SavedKey> keys = MMUtil.getCustomKeybinds().getSavedKeys();
		for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keys.entrySet()) {
			if(entry.getValue().getKeyType() != 1)
				continue;
			boolean b = Keyboard.isKeyDown(entry.getValue().getKey());
			MMUtil.getCustomKeybinds().onKey(entry.getKey(), b);
		}
	}

	/**
	 * MegaMod third person overwrite
	 * @param settings used variable in original
	 * @param value value that it was set to in original code
	 */
	@Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;thirdPersonView:Z", opcode = Opcodes.PUTFIELD))
	private void constant63(GameSettings settings, boolean value) {
		int third = MMUtil.thirdPersonView;
		third++;
		if(third > 2)
			third = 0;
		MMUtil.thirdPersonView = third;
		gameSettings.thirdPersonView = third != 0 && (third == 1 ? true : true);
	}

	/**
	 * MegaMod third person overwrite
	 * @param ci callback
	 */
	@Inject(method = "run", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;thirdPersonView:Z", shift = At.Shift.AFTER))
	private void redirectThird(CallbackInfo ci) {
		MMUtil.thirdPersonView = 0;
	}

	/**
	 * MegaMod always multiplayer overwrite
	 * @param minecraft mc
	 * @return is MP bool
	 */
	@Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isMultiplayerWorld()Z"))
	private boolean isMultiplayer(Minecraft minecraft) {
		return true;
	}

	/**
	 * MegaMod custom keybinds trigger
	 * @param ci callback
	 */
	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 2, shift = At.Shift.BEFORE))
	private void onKeyPress(CallbackInfo ci) {
		if(Keyboard.getEventKey() == 63) {
			int third = MMUtil.thirdPersonView+1;
			if(third > 2)
				third = 0;
			MMUtil.thirdPersonView = third;
		}
		if(Keyboard.getEventKey() == gameSettings.keyBindJump.keyCode)
			MMUtil.getCustomKeybinds().onKey("Jump", true);
		Map<String, CustomKeybinds.SavedKey> keys = MMUtil.getCustomKeybinds().getSavedKeys();
		for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keys.entrySet()) {
			if(entry.getValue().getKeyType() != 0)
				continue;
			if(Keyboard.getEventKey() == entry.getValue().getKey()) {
				MMUtil.getCustomKeybinds().onKey(entry.getKey(), true);
			}
		}
	}
}
