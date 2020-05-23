package net.oldhaven.mixins.shader.glsl;

import net.minecraft.src.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer_Process {
    @Shadow float fogColorRed;
    @Shadow float fogColorGreen;
    @Shadow float fogColorBlue;

    /*private Water_Config water_config;
    private Shader water;
    private int water_timer;
    private int water_colorMap;
    private int water_reflectedColorMap;
    private int water_stencilMap;
    private int water_color;
    private Shader black;
    private Shader white;
    private int white_inverse_view;
    private Shader transparency;
    private int transp_render_v3;
    private int transp_water;
    private int transp_waterfall;
    private int transp_waterfall_color;
    private FBO framebuffer;
    private long previous_time = 0L;
    private float timer = 0.0F;
    private double last_water_height = 0.0D;
    private int on_ground_delay = 0;
    private ByteBuffer matrixbuffer = ByteBuffer.allocateDirect(64);
    private ByteBuffer projectionmatrixbuffer = ByteBuffer.allocateDirect(64);
    boolean second_renderpass = true;
    boolean third_person = false;
    boolean view_bbb = false;
    @Inject(method = "<init>", at=@At("RETURN"))
    private void init(Minecraft minecraft, CallbackInfo ci) {
        this.water_config = new Water_Config();
        if (this.water_config.water_render_mode == 1) {
            this.second_renderpass = false;
        } else {
            this.second_renderpass = true;
        }

        System.out.println("Initializing shaders...");
        this.j.z.h = false;
        this.water = new Shader("/shader/water");
        this.water_timer = this.water.initValue1f("timer");
        this.water_colorMap = this.water.initValue1i("colorMap");
        this.water_reflectedColorMap = this.water.initValue1i("reflectedColorMap");
        this.water_stencilMap = this.water.initValue1i("stencilMap");
        this.water_color = this.water.initValueVec3f("water_color");
        this.black = new Shader("/shader/color_black");
        this.white = new Shader("/shader/color_white");
        this.white_inverse_view = this.white.initValueMat4f("inverse_view");
        this.transparency = new Shader("/shader/transparency");
        this.transp_render_v3 = this.transparency.initValue1f("setting_v3");
        this.transp_water = this.transparency.initValue1f("water_transparency");
        this.transp_waterfall = this.transparency.initValue1f("waterfall_transparency");
        this.transp_waterfall_color = this.transparency.initValue1f("waterfall_color");
        System.out.println("Initializing FBO...");
        this.framebuffer = new FBO(this.j.d, this.j.e, this.water_config.reflection_resolution, this.water_config.anti_aliasing);
    }*/

    @Inject(method = "updateCameraAndRender", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;limitFramerate:I", ordinal = 3))
    private void shaderProcess(float v, CallbackInfo ci) {
        //Shader.processScene(this.fogColorRed, this.fogColorGreen, this.fogColorBlue);
    }
}
