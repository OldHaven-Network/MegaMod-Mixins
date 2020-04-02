package net.oldhaven.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.customs.CustomGameSettings;
import net.oldhaven.MegaMod;
import org.lwjgl.opengl.GL11;

public class CustomGuiButton extends GuiButton {
    public static class GuiSmallButton extends CustomGuiButton {
        private final CustomEnumOptions enumOptions;

        public GuiSmallButton(int var1, int var2, int var3, CustomEnumOptions var4, String var5) {
            super(var1, var2, var3, 150, 20, var5);
            this.enumOptions = var4;
        }

        public void keyTyped(char var1, int var2) { }

        @Override
        public void mouseReleased(int i, int i1) {
            super.mouseReleased(i, i1);
            if(mousePressed(null, i, i1)) {
                String s = this.returnEnumOptions().getEnumString();
                Object option = MegaMod.getInstance().getCustomGameSettings().getOption(s);
                boolean b = (option != null && String.valueOf(option).equals("1"));
                MegaMod.getInstance().getCustomGameSettings().setOption(s, b ? 0 : 1);
            }
        }

        @Override
        public void drawButton(Minecraft var1, int i, int j) {
            if (this.enabled2) {
                String onOff = "";
                if(returnEnumOptions() != null) {
                    String s = this.returnEnumOptions().getEnumString();
                    onOff = ": OFF";
                    Object option = MegaMod.getInstance().getCustomGameSettings().getOption(s);
                    if(option != null && String.valueOf(option).equals("1"))
                        onOff = ": ON";
                }

                FontRenderer var4 = var1.fontRenderer;
                GL11.glBindTexture(3553, var1.renderEngine.getTexture("/gui/gui.png"));
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                boolean var5 = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
                int var6 = this.getHoverState(var5);
                CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
                int value = (int)(gs.getOptionF("Button Outline") * 11.0F);
                if(value > 0) {
                    int color;
                    switch(value) {
                        case 1: color = 0x3232a8;break;
                        case 2: color = 0xa032a8;break;
                        case 3: color = 0xa8324e;break;
                        case 4: color = 0x32a2a8;break;
                        case 5: color = 0x32a86d;break;
                        case 6: color = 0xa8a432;break;
                        case 7: color = 0xa87532;break;
                        case 8: color = 0xbdbdbd;break;
                        case 9: color = 0x636363;break;
                        case 10: color = 0x000000;break;
                        case 11: color = Integer.decode(gs.getOptionS("Button ADV Color"));break;
                        default: color = 0xffffff;break;
                    }
                    if (i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height)
                        drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, adjustAlpha(color, 255));
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var6 * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
                this.mouseDragged(var1, i, j);
                if (!this.enabled) {
                    this.drawCenteredString(var4, this.displayString + onOff, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
                } else if (var5) {
                    this.drawCenteredString(var4, this.displayString + onOff, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 16777120);
                } else {
                    this.drawCenteredString(var4, this.displayString + onOff, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 14737632);
                }

            }
        }

        public CustomEnumOptions returnEnumOptions() {
            return this.enumOptions;
        }
    }

    public int adjustAlpha(int rgb, int alpha) {
        int red = (rgb>>16) &0xff;
        int green = (rgb>>8) &0xff;
        int blue = (rgb>>0) &0xff;
        return (alpha&0x0ff)<<24 | red<<16 | green<<8 | blue;
    }

    public static class GuiTextField extends GuiButton {
        private String text;
        private int maxStringLength;
        private int cursorCounter;
        public boolean isFocused = false;
        private GuiScreen parentGuiScreen;
        private int xPos;
        private int yPos;
        private int width;
        private int height;
        private FontRenderer fontRenderer;
        private final CustomEnumOptions enumOption;

        public GuiTextField(GuiScreen screen, FontRenderer var1, int oridnal, int x, int y, CustomEnumOptions enumOptions, String var7) {
            super(oridnal, x, y, 150, 20, var7);
            this.enumOption = enumOptions;
            this.text = var7;
            this.xPos = x;
            this.yPos = y;
            this.width = 150;
            this.height = 20;
            this.fontRenderer = var1;
            this.parentGuiScreen = screen;
        }

        public void setText(String var1) {
            this.text = var1;
        }

        public String getText() {
            return this.text;
        }

        public void updateCursorCounter() {
            ++this.cursorCounter;
        }

        public void keyTyped(char var1, int var2) {
            if (this.enabled && this.isFocused) {
                String theSacredText = this.text;
                if (var1 == '\t') {
                    this.parentGuiScreen.selectNextField();
                }

                if (var1 == 22) {
                    String var3 = GuiScreen.getClipboardString();
                    if (var3 == null) {
                        var3 = "";
                    }

                    int var4 = 32 - this.text.length();
                    if (var4 > var3.length()) {
                        var4 = var3.length();
                    }

                    if (var4 > 0) {
                        this.text = this.text + var3.substring(0, var4);
                    }
                }

                if (var2 == 14 && this.text.length() > 0) {
                    this.text = this.text.substring(0, this.text.length() - 1);
                }

                if (ChatAllowedCharacters.allowedCharacters.indexOf(var1) >= 0 && (this.text.length() < this.maxStringLength || this.maxStringLength == 0)) {
                    this.text = this.text + var1;
                }
                if(this.text.length() < enumOption.getMinString())
                    this.text = theSacredText;
                if(this.text.length() > enumOption.getMaxString())
                    this.text = theSacredText;
            }
        }

        public boolean mousePressed(Minecraft var1, int var2, int var3) {
            boolean var4 = this.enabled && var2 >= this.xPos && var2 < this.xPos + this.width && var3 >= this.yPos && var3 < this.yPos + this.height;
            this.setFocused(var4);
            return var4;
        }

        public void setFocused(boolean var1) {
            if (var1 && !this.isFocused) {
                this.cursorCounter = 0;
            }

            this.isFocused = var1;
        }

        public void drawButton(Minecraft var1, int i, int j) {
            if(this.enabled) {
                this.updateCursorCounter();
            }
            this.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
            this.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
            if (this.enabled) {
                boolean b = this.isFocused && this.cursorCounter / 6 % 2 == 0;
                this.drawString(this.fontRenderer, this.text + (b ? "_" : ""), this.xPos + 4, this.yPos + (this.height - 8) / 2, 14737632);
            } else {
                this.drawString(this.fontRenderer, this.text, this.xPos + 4, this.yPos + (this.height - 8) / 2, 7368816);
            }
        }

        public void setMaxStringLength(int var1) {
            this.maxStringLength = var1;
        }
    }

    public static class GuiSlider extends GuiButton {
        private CustomEnumOptions idFloat = null;
        public float sliderValue = 1.0F;
        public boolean dragging = false;
        public GuiSlider(int var1, int var2, int var3, CustomEnumOptions var4, String var5, float var6) {
            super(var1, var2, var3, 150, 20, var5);
            this.idFloat = var4;
            this.sliderValue = var6;
            CustomGameSettings gs = MegaMod.getInstance().getCustomGameSettings();
            Object nullable = gs.getOption(var4.getEnumString());
            if(nullable == null)
                gs.setOption(var4.getEnumString(), var4.getDefaultValue());
            this.doDisplayString();
        }

        private void doDisplayString() {
            String option = this.idFloat.getEnumString();
            String end = this.idFloat.getSlideEnd();
            float add = this.idFloat.getAdd();
            float times = this.idFloat.getTimes();
            String startString = this.idFloat.getStartString();
            int i = (int)((this.sliderValue * times) + add);
            System.out.println(i);
            if(this.idFloat.getValues() != null) {
                this.displayString = this.sliderValue == 0.0 ? option + ": "+ startString : option + ": " + this.idFloat.getValues()[i] + end;
            } else
                this.displayString = this.sliderValue == 0.0 ? option + ": "+ startString : option + ": " + i + end;
        }

        protected int getHoverState(boolean var1) {
            return 0;
        }

        @Override
        protected void mouseDragged(Minecraft minecraft, int var2, int var3) {
            if (this.enabled2) {
                if (this.dragging) {
                    this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (float)(this.width - 8);
                    if (this.sliderValue < 0.0F) {
                        this.sliderValue = 0.0F;
                    }

                    if (this.sliderValue > 1.0F) {
                        this.sliderValue = 1.0F;
                    }

                    MegaMod.getInstance().getCustomGameSettings().setOption(this.idFloat.getEnumString(), this.sliderValue);
                    this.doDisplayString();
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
        }

        public boolean mousePressed(Minecraft var1, int var2, int var3) {
            if (super.mousePressed(var1, var2, var3)) {
                this.sliderValue = (float) (var2 - (this.xPosition + 4)) / (float) (this.width - 8);
                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }

                MegaMod.getInstance().getCustomGameSettings().setOption(this.idFloat.getEnumString(), this.sliderValue);
                this.doDisplayString();
                this.dragging = true;
                return true;
            }
            return false;
        }
        public void mouseReleased(int var1, int var2) {
            this.dragging = false;
        }
    }
    public CustomGuiButton(int var1, int var2, int var3, String var4) {
        this(var1, var2, var3, 200, 20, var4);
    }
    public CustomGuiButton(int var1, int var2, int var3, int var4, int var5, String var6) {
        super(var1, var2, var3, var4, var5, var6);
    }
}
