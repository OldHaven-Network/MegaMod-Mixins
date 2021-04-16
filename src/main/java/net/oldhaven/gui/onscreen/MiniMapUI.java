package net.oldhaven.gui.onscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.BlockColor;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.customs.util.Vec2D;
import net.oldhaven.devpack.SingleCallback;
import org.lwjgl.opengl.GL11;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static net.oldhaven.customs.util.WorldUtil.*;

public class MiniMapUI extends OnScreenUI {
    Executor executor = Executors.newCachedThreadPool();

    public RenderItem renderItem;
    public static int mapSize = 56;
    int startAt = 0;

    int pointerStart = 72;
    int pointerSize = 9;

    int tick = 0;
    public void inGameTick(final Minecraft mc, SingleCallback<List<BlockToDraw>> success) {
        tick++;
        if(tick < 2)
            return;
        tick = 0;

        final int mapSize = MiniMapUI.mapSize;
        executor.execute(() -> {
            World world = mc.thePlayer.worldObj;
            Vec3D pos = mc.thePlayer.getPosition(1.0F);
            List<MiniMapUI.BlockToDraw> newList = new ArrayList<>();
            int pX = (int) pos.xCoord;
            int pY = (int) pos.yCoord;
            int pZ = (int) pos.zCoord;
            int radius = mapSize/2;
            if(ModOptions.MINIMAP_STYLE.getAsInt() == 0)
                radius -= 7;
            boolean renderSelfPlane = ModOptions.MINIMAP_RENDER_SELF_PLANE.getAsBool();
            if(renderSelfPlane)
                pY -= 2;
            if(MMUtil.mapOpen)
                radius += 150;

            int big = radius;
            if(ModOptions.MINIMAP_ROTATE.getAsBool())
                big = (int)(radius * 1.4F);

            for (int i=-big; i < big;i++) {
                int x = pX + i;
                for (int p = -big; p < big; p++) {
                    int z = pZ + p;
                    int id;
                    int finalId = 0;
                    int newY = pY;
                    if(renderSelfPlane) {
                        if((id = world.getBlockId(x, pY, z)) <= 0)
                            continue;
                        finalId = id;
                    } else {
                        if(pY < 63) {
                            for(int up=0;up < 100;up++) {
                                if((id=getBlockAbove(world, x, pY+up, z)) != 0) {
                                    if(!isBlockStuck(world, x, pY+up, z)) {
                                        finalId = id;
                                        newY = pY+up;
                                    }
                                }
                            }
                            if(finalId == 0) {
                                for (int down=0;down < 100;down++) {
                                    if((id=world.getBlockId(x, pY-down, z)) != 0) {
                                        if(!isBlockStuck(world, x, pY-down, z)) {
                                            finalId = id;
                                            newY = pY - down;
                                            break;
                                        }
                                    }
                                }
                                if (finalId == 0)
                                    continue;
                            }
                        } else {
                            finalId = getTopBlockAt(world, x, z);
                            if (finalId == 0)
                                continue;
                        }
                    }
                    double[] pt = {pX - x, pZ - z};
                    if(ModOptions.MINIMAP_ROTATE.getAsBool()) {
                        double rad = Math.toRadians(180.0F - mc.thePlayer.rotationYaw);
                        double middle = 0D;
                        AffineTransform.getRotateInstance(
                                rad, middle, middle
                        ).transform(pt, 0, pt, 0, 1);
                    }
                    pt[0] = pt[0] + radius;
                    pt[1] = pt[1] + radius;
                    if (pt[0] < 0 || pt[0] > mapSize ||
                        pt[1] < 0 || pt[1] > mapSize)
                        continue;
                    ItemStack stack = new ItemStack(finalId, 1, 0);
                    MiniMapUI.BlockToDraw draw = new MiniMapUI.BlockToDraw(
                            stack, world.getBlockMetadata(x, newY, z), pt);
                    //xzArray.add(xz);
                    newList.add(draw);
                }
            }
            success.run(newList);
        });
    }

    private boolean isBlockACliff(World world, int x, int y, int z) {
        int left = world.getBlockId(x-1, y, z);
        int right = world.getBlockId(x+1, y, z);
        int fwd = world.getBlockId(x, y, z+1);
        int back = world.getBlockId(x, y, z-1);
        return left != 0 || right != 0 || fwd != 0 || back != 0;
    }

    private enum MiniMap {
        LARGE_BG(64, Vec2D.create(0, 0), Vec2D.create(64, 0)),
        SMALL_BG(56, Vec2D.create(129, 0), Vec2D.create(184, 0));

        final int size;
        final Vec2D bg, fg;
        MiniMap(int size, Vec2D bg, Vec2D fg) {
            this.size = size;
            this.fg = fg;
            this.bg = bg;
        }
    }

    public void draw(ScaledResolution sc, RenderItem renderItem) {
        if(!ModOptions.MINIMAP_ENABLED.getAsBool())
            return;

        mapSize = ModOptions.MINIMAP_SIZE.getAsInt();
        if(mapSize < 40)
            mapSize = 40;

        this.renderItem = renderItem;
        int screenWidth = sc.getScaledWidth();
        int horiz = (int) (screenWidth-mapSize*1.1F);
        int vert = 6;

        Minecraft mc = getMinecraft();

        int texId = mc.renderEngine.getTexture("/gui/map.png");
        this.drawBackground(mc, horiz, vert);
        this.drawBlocks(mc, horiz, vert);
        //this.drawForeground(mc, texId, horiz, vert);

        if(ModOptions.MINIMAP_SHOW_COORDS.getAsBool())
            drawCoords(mc, horiz, vert);

        if(ModOptions.MINIMAP_STYLE.getAsInt() != 0) {
            this.drawPointer(mc, texId, horiz, vert);
            if(ModOptions.MINIMAP_COMPASS.getAsBool())
                this.drawDirections(mc, texId, horiz, vert);
        }
    }

    public static double getFacing(Minecraft mc) {
        return ((double)(
                mc.thePlayer.rotationYaw * 4.0F / 360.0F
        ) + 0.5D); // from GuiIngame
    }

    private void drawDirections(Minecraft mc, int texId, int horiz, int vert) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);

        boolean shadows = ModOptions.MINIMAP_COMPASS_SHADOW.getAsBool();
        boolean highlight = ModOptions.MINIMAP_HIGLIGHT_DIR.getAsBool();
        String hex = ModOptions.MINIMAP_COMPASS_COLOR_HEX.getAsString();

        int gH, gV;

        int pF = (MathHelper.floor_double(getFacing(mc)) & 3);
        // how close should the letters be
        int closer = -2;
        // re-center horiz
        horiz = horiz - (6/2);

        float rad = 180.0F - mc.thePlayer.rotationYaw;
        int halfH = horiz + mapSize/2+3;
        int halfV = vert + mapSize/2;

        GL11.glPushMatrix();
        if(ModOptions.MINIMAP_ROTATE.getAsBool()) {
            GL11.glTranslated(halfH, halfV, 0.0F);
            GL11.glRotatef(rad, 0, 0, 1);
            GL11.glTranslated(-halfH, -halfV, 0.0F);
        }

        gH = horiz + (mapSize / 2);
        gV = vert + 6 + closer; // north
        if(shadows)
            drawCompass(2, pF,gH+1, gV+1,
                    6+9, 64,
                    6, 8,
                    0, hex, highlight);
        drawCompass(2, pF, gH, gV,
                6+9, 64,
                6, 8,
                1, hex, highlight);
        gH = horiz + mapSize - 11 - closer;
        gV = vert + (mapSize / 2) - (8/2); // east
        if(shadows)
            drawCompass(3, pF, gH+1, gV+1,
                    6+9+6, 64,
                    6, 8,
                    0, hex, highlight);
        drawCompass(3, pF, gH, gV,
                6+9+6, 64,
                6, 8,
                1, hex, highlight);
        gH = horiz + 9 + closer;
        gV = vert + (mapSize / 2) - (8/2); // west
        if(shadows)
            drawCompass(1, pF,gH+1, gV+1,
                    6, 64,
                    9, 8,
                    0, hex, highlight);
        drawCompass(1, pF, gH, gV,
                6, 64,
                9, 8,
                1, hex, highlight);
        gH = horiz + (mapSize / 2);
        gV = vert + mapSize - (6*2) + closer; // south
        if(shadows)
            drawCompass(0, pF, gH+1, gV+1,
                    0, 64,
                    6, 8,
                    0, hex, highlight);
        drawCompass(0, pF, gH, gV,
                0, 64,
                6, 8,
                1, hex, highlight);

        GL11.glPopMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawCoords(Minecraft mc, int horiz, int vert) {
        Vec3D pos = mc.thePlayer.getPosition(1.0F);
        String coords = (int) pos.xCoord + " " + (int) pos.yCoord + " " + (int) pos.zCoord;
        int half = mapSize/2;
        horiz = horiz + half;
        vert = vert + mapSize + 12;
        drawCenteredString(mc.fontRenderer, coords, horiz, vert, 0xffffffff);
    }

    private void drawCompass(int facing, int pF, int x, int z, int pX, int pZ, int pH, int pV, float rgb, String hex, boolean highlight) {
        if(rgb == 0) {
            GL11.glColor4f(rgb, rgb, rgb, 1.0F);
        } else {
            if (highlight) {
                if (facing == pF) {
                    if (!hex.isEmpty())
                        adjustAlpha(Integer.decode(hex));
                    else
                        GL11.glColor3f(rgb, rgb, rgb);
                } else {
                    GL11.glColor3f(0.0F, 0.0F, 0.0F);
                }
            } else {
                if (!hex.isEmpty())
                    adjustAlpha(Integer.decode(hex));
                else
                    GL11.glColor3f(rgb, rgb, rgb);
            }
        }
        drawTexturedModalRect(x, z, pX, pZ, pH, pV);
    }

    private void drawBackground(Minecraft mc, int horiz, int vert) {
        //int texId = mc.renderEngine.getTexture("/gui/map.png");
        String hexValue = ModOptions.MINIMAP_BORDER_COLOR_HEX.getAsString();
        drawRect(horiz-1, vert-1, horiz+mapSize+2, vert+mapSize+2, hexValue);

        /*GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texId);
        drawTexturedModalRect(horiz, vert, startAt, 0, mapSize, mapSize);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);*/
        return; //texId;
    }

    /*private void drawForeground(Minecraft mc, int texId, int horiz, int vert) {
        String hexValue = ModOptions.MINIMAP_BORDER_COLOR_HEX.getAsString();
        if(!hexValue.isEmpty())
            adjustAlpha(Integer.decode(hexValue));
        else
            GL11.glColor3f(0.0F, 0.0F, 0.0F);
        drawRect(horiz, vert, 1, vert+mapSize-1, hexValue);
        drawRect(horiz, vert, horiz+mapSize, 1, hexValue);
        drawRect(horiz, vert+mapSize-1, horiz+mapSize, 1, hexValue);
        //drawRect(horiz, vert, horiz, 1);
        if(null == null)
            return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if(!hexValue.isEmpty())
            adjustAlpha(Integer.decode(hexValue));
        else
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texId);
        drawTexturedModalRect(horiz, vert, startAt+mapSize, 0, mapSize, mapSize);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
    }*/

    protected void drawRect(double x1, double y1, double x2, double y2, String hexValue) {
        Tessellator var10 = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        if(!hexValue.isEmpty())
            adjustAlpha(Integer.decode(hexValue));
        else
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
        var10.startDrawingQuads();
        var10.addVertex(x1, y2, 0.0D);
        var10.addVertex(x2, y2, 0.0D);
        var10.addVertex(x2, y1, 0.0D);
        var10.addVertex(x1, y1, 0.0D);
        var10.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    private void drawPointer(Minecraft mc, int texId, int horiz, int vert) {
        float rad = (180.0F+90.0F) - mc.thePlayer.rotationYaw;
        if(ModOptions.MINIMAP_ROTATE.getAsBool())
            rad = 90.0F;
        int gH = horiz + mapSize/2;
        int gV = vert + mapSize/2;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //String hexValue = ModOptions.MINIMAP_BORDER_COLOR_HEX.getAsString();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPushMatrix();
        GL11.glTranslated(gH, gV, 0.0F);
        GL11.glRotatef(-rad, 0, 0, 1);
        GL11.glTranslated(-gH, -gV, 0.0F);
        mc.renderEngine.bindTexture(texId);
        drawTexturedModalRect(gH, gV, pointerStart, 0, pointerSize, 1);

        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void adjustAlpha(int rgb) {
        float red = (rgb>>16) &0xff;
        float green = (rgb>>8) &0xff;
        float blue = (rgb>>0) &0xff;
        GL11.glColor3f(red/255, green/255, blue/255);
    }

    public static class BlockToDraw {
        public BlockToDraw(ItemStack stack, int metadata, double[] xy) {
            this.block = stack;
            this.metadata = metadata;
            this.x = xy[0];
            this.z = xy[1];
        }
        ItemStack block;
        int metadata;
        double x; double z;
    } private List<BlockToDraw> list = new ArrayList<>();
    public void prepareDrawBlock(List<BlockToDraw> prepMap) {
        this.list = prepMap;
    }

    public void drawBlocks(Minecraft mc, int horiz, int vert) {
        float rad = 90.0F - mc.thePlayer.rotationYaw;
        if(list.isEmpty())
            return;
        int i = 0;
        for(BlockToDraw draw : list) {
            if(ModOptions.MINIMAP_STYLE.getAsInt() == 0) {
                renderItem.renderItemIntoGUI(
                        mc.fontRenderer, mc.renderEngine,
                        draw.block,
                        (int) (horiz+ draw.x),
                        (int) (vert + draw.z));
            } else {
                BlockColor bColor = BlockColor.getBlockColor(
                        draw.block.itemID, draw.metadata);
                double gH = horiz + mapSize - draw.x;
                double gV = vert + mapSize - draw.z;
                //if(!ModOptions.MINIMAP_LARGE.getAsBool())
                    //gH -= 2;
                GL11.glPushMatrix();
                GL11.glTranslated(gH, gV, 0);
                GL11.glRotatef(rad,0, 0, 1.0F);
                GL11.glTranslated(-gH, -gV, 0);
                drawRect(gH-1, gV-1,  gH+1, gV+1, bColor.argb);
                GL11.glPopMatrix();
            }
            i++;
        }
        if(ModOptions.MINIMAP_STYLE.getAsInt() == 0)
            RenderHelper.disableStandardItemLighting();
    }
}
