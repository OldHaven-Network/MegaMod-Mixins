package net.oldhaven.customs.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.*;
import java.util.ArrayList;

public class Shader {
    public static ByteBuffer shadersBuffer = null;
    public static ShortBuffer shadersShortBuffer = null;
    public static short[] shadersData = new short[]{-1, 0};
    public static boolean E = false;
    public static boolean showRestartAlert = true;
    public static int activeProgram = 0;
    public static int baseProgram = 0;
    public static int baseProgramNoT2D = 0;
    public static int baseProgramBM = 0;
    public static int finalProgram = 0;
    public static int entityAttrib = -1;
    public static boolean useMSAA = false;
    public static int msaaSamples = 4;
    public static boolean useFSAA = false;
    public static int fsaaAmount = 2;
    private static Pbuffer pbuffer;
    private static ByteBuffer pixels;
    private static int renderWidth = 0;
    private static int renderHeight = 0;
    private static int renderType = 0;
    private static float[] sunPosition = new float[3];
    private static float[] moonPosition = new float[3];
    private static Shader.LightSource[] lightSources;
    private static ArrayList j = new ArrayList();
    public static int displayTextureId = 0;
    public static int baseTextureId = 0;
    public static int depthTextureId = 0;
    public static int depthTexture2Id = 0;
    public static Minecraft x = null;
    public static final int RENDER_TYPE_UNKNOWN = 0;
    public static final int RENDER_TYPE_TERRAIN = 1;
    private static int setupProgram(String s, String s1, String s2, String s3) {
        int i = ARBShaderObjects.glCreateProgramObjectARB();
        int k = 0;
        int l = 0;
        if (i != 0) {
            k = createVertShader(s, s1);
            l = createFragShader(s2, s3);
        }

        if (k == 0 && l == 0) {
            i = 0;
        } else {
            if (k != 0) {
                ARBShaderObjects.glAttachObjectARB(i, k);
            }

            if (l != 0) {
                ARBShaderObjects.glAttachObjectARB(i, l);
            }

            if (entityAttrib >= 0) {
                ARBVertexShader.glBindAttribLocationARB(i, entityAttrib, "mc_Entity");
            }

            ARBShaderObjects.glLinkProgramARB(i);
            ARBShaderObjects.glValidateProgramARB(i);
            printLogInfo(s, i);
        }

        return i;
    }

    public static void setEntity(int i, int j, int k) {
        Shader.shadersData[0] = (short)i;
        Shader.shadersData[1] = (short)(j + k * 16);
    }

    public static void useProgram(int i) {
        ARBShaderObjects.glUseProgramObjectARB(i);
        activeProgram = i;
        if (i != 0) {
            if(x.thePlayer != null) {

            }
            int zero = ARBShaderObjects.glGetUniformLocationARB(i, "sampler0");
            ARBShaderObjects.glUniform1iARB(zero, 0);
            int one = ARBShaderObjects.glGetUniformLocationARB(i, "sampler1");
            ARBShaderObjects.glUniform1iARB(one, 1);
            int two = ARBShaderObjects.glGetUniformLocationARB(i, "sampler2");
            ARBShaderObjects.glUniform1iARB(two, 2);
            int i1 = GL11.glGetInteger(2917);
            int j1 = ARBShaderObjects.glGetUniformLocationARB(i, "fogMode");
            ARBShaderObjects.glUniform1iARB(j1, i1);
            int k1 = ARBShaderObjects.glGetUniformLocationARB(i, "renderType");
            ARBShaderObjects.glUniform1iARB(k1, renderType);
            int l1 = ARBShaderObjects.glGetUniformLocationARB(i, "sunPosition");
            ARBShaderObjects.glUniform3fARB(l1, sunPosition[0], sunPosition[1], sunPosition[2]);
            int i2 = ARBShaderObjects.glGetUniformLocationARB(i, "moonPosition");
            ARBShaderObjects.glUniform3fARB(i2, moonPosition[0], moonPosition[1], moonPosition[2]);
            ItemStack itemstack = x.thePlayer.inventory.getCurrentItem();
            int j2, i3, l3, j4;
            if (itemstack != null && (j2 = itemstack.itemID) < lightSources.length && lightSources[j2] != null) {
                i3 = ARBShaderObjects.glGetUniformLocationARB(i, "heldLight.itemId");
                ARBShaderObjects.glUniform1iARB(i3, j2);
                l3 = ARBShaderObjects.glGetUniformLocationARB(i, "heldLight.magnitude");
                ARBShaderObjects.glUniform1fARB(l3, lightSources[j2].magnitude);
                j4 = ARBShaderObjects.glGetUniformLocationARB(i, "heldLight.specular");
                ARBShaderObjects.glUniform4fARB(j4, lightSources[j2].specular[0], lightSources[j2].specular[1], lightSources[j2].specular[2], lightSources[j2].specular[3]);
            } else {
                i3 = ARBShaderObjects.glGetUniformLocationARB(i, "heldLight.itemId");
                ARBShaderObjects.glUniform1iARB(i3, -1);
                l3 = ARBShaderObjects.glGetUniformLocationARB(i, "heldLight.magnitude");
                ARBShaderObjects.glUniform1fARB(l3, 0.0F);
            }

            i3 = ARBShaderObjects.glGetUniformLocationARB(i, "worldTime");
            ARBShaderObjects.glUniform1iARB(i3, (int)(x.theWorld.getWorldInfo().getWorldTime() % 24000L));
            l3 = ARBShaderObjects.glGetUniformLocationARB(i, "aspectRatio");
            ARBShaderObjects.glUniform1fARB(l3, (float)renderWidth / (float)renderHeight);
            j4 = ARBShaderObjects.glGetUniformLocationARB(i, "displayWidth");
            ARBShaderObjects.glUniform1fARB(j4, (float)renderWidth);
            int k4 = ARBShaderObjects.glGetUniformLocationARB(i, "displayHeight");
            ARBShaderObjects.glUniform1fARB(k4, (float)renderHeight);
            int l4 = ARBShaderObjects.glGetUniformLocationARB(i, "near");
            ARBShaderObjects.glUniform1fARB(l4, 0.05F);
            int i5 = ARBShaderObjects.glGetUniformLocationARB(i, "far");
            ARBShaderObjects.glUniform1fARB(i5, (float)(256 >> x.gameSettings.renderDistance));
        }
    }

    public static void setCelestialPosition() {
        FloatBuffer floatbuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
        GL11.glGetFloat(2982, floatbuffer);
        float[] af = new float[16];
        floatbuffer.get(af, 0, 16);
        sunPosition = multiplyMat4xVec4(af, new float[]{0.0F, 100.0F, 0.0F, 0.0F});
        moonPosition = multiplyMat4xVec4(af, new float[]{0.0F, -100.0F, 0.0F, 0.0F});
    }

    private static float[] multiplyMat4xVec4(float[] af, float[] af1) {
        float[] af2 = new float[]{af[0] * af1[0] + af[4] * af1[1] + af[8] * af1[2] + af[12] * af1[3], af[1] * af1[0] + af[5] * af1[1] + af[9] * af1[2] + af[13] * af1[3], af[2] * af1[0] + af[6] * af1[1] + af[10] * af1[2] + af[14] * af1[3], af[3] * af1[0] + af[7] * af1[1] + af[11] * af1[2] + af[15] * af1[3]};
        return af2;
    }

    private static int createTexture(int i, int k, boolean flag) throws OutOfMemoryError {
        int l = GL11.glGenTextures();
        GL11.glBindTexture(3553, l);
        ByteBuffer bytebuffer;
        if (flag) {
            bytebuffer = ByteBuffer.allocateDirect(i * k * 4 * 4);
            GL11.glTexImage2D(3553, 0, 6402, i, k, 0, 6402, 5126, bytebuffer);
        } else {
            bytebuffer = ByteBuffer.allocateDirect(i * k * 4);
            GL11.glTexImage2D(3553, 0, 6408, i, k, 0, 6408, 5121, bytebuffer);
        }

        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        return l;
    }

    public static void processScene(float f, float f1, float f2) {
        if (!x.gameSettings.anaglyph && finalProgram != 0) {
            GL11.glBindTexture(3553, baseTextureId);
            GL11.glCopyTexImage2D(3553, 0, 6408, 0, 0, renderWidth, renderHeight, 0);
            GL13.glActiveTexture(33985);
            GL11.glBindTexture(3553, depthTextureId);
            GL13.glActiveTexture(33986);
            GL11.glBindTexture(3553, depthTexture2Id);
            GL13.glActiveTexture(33984);
            useProgram(finalProgram);
            GL11.glClearColor(f, f1, f2, 0.0F);
            GL11.glClear(16384);
            GL11.glDisable(2929);
            GL11.glDisable(3042);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, (double)renderWidth, (double)renderHeight, 0.0D, -1.0D, 1.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glBegin(7);
            GL11.glTexCoord2f(0.0F, 1.0F);
            GL11.glVertex3f(0.0F, 0.0F, 0.0F);
            GL11.glTexCoord2f(0.0F, 0.0F);
            GL11.glVertex3f(0.0F, (float)renderHeight, 0.0F);
            GL11.glTexCoord2f(1.0F, 0.0F);
            GL11.glVertex3f((float)renderWidth, (float)renderHeight, 0.0F);
            GL11.glTexCoord2f(1.0F, 1.0F);
            GL11.glVertex3f((float)renderWidth, 0.0F, 0.0F);
            GL11.glEnd();
            GL11.glEnable(2929);
            useProgram(0);
        }

    }

    public static void updateDisplay(Minecraft minecraft) {
        if (useFSAA && pixels != null) {
            pixels.rewind();
            GL11.glReadPixels(0, 0, renderWidth, renderHeight, 6407, 5121, pixels);

            try {
                Display.makeCurrent();
            } catch (LWJGLException var3) {
                var3.printStackTrace();
            }

            GL11.glBindTexture(3553, displayTextureId);
            GL11.glTexImage2D(3553, 0, 6407, renderWidth, renderHeight, 0, 6407, 5121, pixels);
            GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            GL11.glClear(16384);
            GL11.glDisable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glViewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);
            GL11.glOrtho(0.0D, (double)minecraft.displayWidth, (double)minecraft.displayHeight, 0.0D, -1.0D, 1.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glBegin(7);
            GL11.glTexCoord2f(0.0F, 1.0F);
            GL11.glVertex3f(0.0F, 0.0F, 0.0F);
            GL11.glTexCoord2f(0.0F, 0.0F);
            GL11.glVertex3f(0.0F, (float)minecraft.displayHeight, 0.0F);
            GL11.glTexCoord2f(1.0F, 0.0F);
            GL11.glVertex3f((float)minecraft.displayWidth, (float)minecraft.displayHeight, 0.0F);
            GL11.glTexCoord2f(1.0F, 1.0F);
            GL11.glVertex3f((float)minecraft.displayWidth, 0.0F, 0.0F);
            GL11.glEnd();
            GL11.glEnable(2929);
            Display.update();

            try {
                pbuffer.makeCurrent();
            } catch (LWJGLException var2) {
                var2.printStackTrace();
            }
        } else {
            Display.update();
        }

    }

    public static void setUpBuffers() {
        if (x != null) {
            setUpBuffers(x);
        }
    }

    private static void initShaders() {
        entityAttrib = -1;
        baseProgram = setupProgram("/shaders/base.vsh", "#define _ENABLE_GL_TEXTURE_2D\n", "/shaders/base.fsh", "#define _ENABLE_GL_TEXTURE_2D\n");
        baseProgramNoT2D = setupProgram("/shaders/base.vsh", "", "/shaders/base.fsh", "");
        baseProgramBM = setupProgram("/shaders/base.vsh", "#define _ENABLE_GL_TEXTURE_2D\n#define _ENABLE_BUMP_MAPPING\n", "/shaders/base.fsh", "#define _ENABLE_GL_TEXTURE_2D\n#define _ENABLE_BUMP_MAPPING\n");
        finalProgram = setupProgram("/shaders/final.vsh", "", "/shaders/final.fsh", "");
    }

    public static void setUpBuffers(Minecraft minecraft) {
        x = minecraft;
        if (!E) {
            initLightSources();
            if (useMSAA) {
                try {
                    Display.destroy();
                    Display.create((new PixelFormat()).withSamples(msaaSamples));
                    useMSAA = false;
                } catch (LWJGLException var6) {
                    var6.printStackTrace();

                    try {
                        Display.create();
                    } catch (LWJGLException var5) {
                        var5.printStackTrace();
                    }
                }
            }

            initShaders();
            E = true;
        }

        if (useFSAA) {
            try {
                pbuffer = new Pbuffer(minecraft.displayWidth * fsaaAmount, minecraft.displayHeight * fsaaAmount, new PixelFormat(), (RenderTexture)null, (Drawable)null);
                renderWidth = pbuffer.getWidth();
                renderHeight = pbuffer.getHeight();
                pbuffer.makeCurrent();
                pixels = BufferUtils.createByteBuffer(renderWidth * renderHeight * 3);
            } catch (LWJGLException var4) {
                var4.printStackTrace();
            }

            GL11.glDeleteTextures(displayTextureId);
            displayTextureId = createTexture(renderWidth, renderHeight, false);
        } else {
            try {
                renderWidth = minecraft.displayWidth;
                renderHeight = minecraft.displayHeight;
                Display.makeCurrent();
            } catch (LWJGLException var3) {
                var3.printStackTrace();
            }
        }

        GL11.glShadeModel(7425);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glCullFace(1029);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glDeleteTextures(baseTextureId);
        baseTextureId = createTexture(renderWidth, renderHeight, false);
        GL11.glDeleteTextures(depthTextureId);
        depthTextureId = createTexture(renderWidth, renderHeight, false);
        GL11.glDeleteTextures(depthTexture2Id);
        depthTexture2Id = createTexture(renderWidth, renderHeight, false);
    }

    private static void initLightSources() {
        Item[] itemsList = Item.itemsList;
        lightSources = new Shader.LightSource[itemsList.length];

        for(int i = 0; i < itemsList.length; ++i) {
            if (itemsList[i] != null) {
                if (i < Block.blocksList.length && Block.blocksList[i] != null) {
                    lightSources[i] = new Shader.LightSource((float) Block.lightValue[i]);
                    if (Block.blocksList[i] == Block.torchWood) {
                        lightSources[i].setSpecular(1.0F, 0.9F, 0.5F, 1.0F);
                    } else if (Block.blocksList[i] == Block.torchRedstoneActive) {
                        lightSources[i].setSpecular(1.0F, 0.0F, 0.0F, 1.0F);
                    } else if (Block.blocksList[i] == Block.pumpkin) {
                        lightSources[i].setSpecular(1.0F, 1.0F, 0.8F, 1.0F);
                    }
                } else if (itemsList[i] == Item.bucketLava) {
                    lightSources[i] = new Shader.LightSource((float) Block.lightValue[Block.lavaStill.blockID]);
                    lightSources[i].setSpecular(1.0F, 0.5F, 0.0F, 1.0F);
                } else if (itemsList[i] == Item.bucketWater) {
                    lightSources[i] = new Shader.LightSource(2.0F);
                    lightSources[i].setSpecular(0.0F, 0.0F, 0.3F, 1.0F);
                }
            }
        }
    }

    public static void viewport(int i, int k, int l, int i1) {
        if (useFSAA) {
            GL11.glViewport(i * fsaaAmount, k * fsaaAmount, l * fsaaAmount, i1 * fsaaAmount);
        } else {
            GL11.glViewport(i, k, l, i1);
        }

    }

    public static void copyDepthTexture(int i) {
        GL11.glBindTexture(3553, i);
        GL11.glCopyTexImage2D(3553, 0, 6402, 0, 0, renderWidth, renderHeight, 0);
    }

    public static void bindTexture(int i, int k) {
        GL13.glActiveTexture(i);
        GL11.glBindTexture(3553, k);
        GL13.glActiveTexture(33984);
    }

    public static void setRenderType(int i) {
        renderType = i;
        if (activeProgram != 0) {
            int k = ARBShaderObjects.glGetUniformLocationARB(activeProgram, "renderType");
            ARBShaderObjects.glUniform1iARB(k, renderType);
        }
    }

    private static int createVertShader(String s, String s1) {
        int i = ARBShaderObjects.glCreateShaderObjectARB(35633);
        if (i == 0) {
            return 0;
        } else {
            String s2 = s1;

            try {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(s)));

                String s3;
                while((s3 = bufferedreader.readLine()) != null) {
                    if (s3.matches("#version .*")) {
                        s2 = s3 + "\n" + s2;
                    } else {
                        if (s3.matches("attribute [_a-zA-Z0-9]+ mc_Entity.*")) {
                            entityAttrib = 10;
                        }

                        s2 = s2 + s3 + "\n";
                    }
                }
            } catch (Exception var6) {
                return 0;
            }

            ARBShaderObjects.glShaderSourceARB(i, s2);
            ARBShaderObjects.glCompileShaderARB(i);
            printLogInfo(s, i);
            return i;
        }
    }

    private static int createFragShader(String s, String s1) {
        int i = ARBShaderObjects.glCreateShaderObjectARB(35632);
        if (i == 0) {
            return 0;
        } else {
            String s2 = s1;

            try {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(s)));

                String s3;
                while((s3 = bufferedreader.readLine()) != null) {
                    if (s3.matches("#version .*")) {
                        s2 = s3 + "\n" + s2;
                    } else {
                        s2 = s2 + s3 + "\n";
                    }
                }
            } catch (Exception var6) {
                return 0;
            }

            ARBShaderObjects.glShaderSourceARB(i, s2);
            ARBShaderObjects.glCompileShaderARB(i);
            printLogInfo(s, i);
            return i;
        }
    }
    private static boolean printLogInfo(String shader, int i) {
        IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(i, 35716, intbuffer);
        int k = intbuffer.get();
        if (k > 1) {
            ByteBuffer bytebuffer = BufferUtils.createByteBuffer(k);
            intbuffer.flip();
            ARBShaderObjects.glGetInfoLogARB(i, intbuffer, bytebuffer);
            byte[] abyte0 = new byte[k];
            bytebuffer.get(abyte0);
            String s = new String(abyte0);
            System.out.println("Info log for "+shader+":\n" + s);
            return false;
        } else {
            return true;
        }
    }

    private static class LightSource {
        public float magnitude = 0.0F;
        public float[] specular = new float[]{1.0F, 1.0F, 1.0F, 1.0F};

        public void setSpecular(float f, float f1, float f2, float f3) {
            this.specular[0] = f;
            this.specular[1] = f1;
            this.specular[2] = f2;
            this.specular[3] = f3;
        }

        public LightSource(float f) {
            this.magnitude = f;
        }
    }
}
