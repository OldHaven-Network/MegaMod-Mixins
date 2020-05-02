package net.oldhaven.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.CustomKeybinds;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GuiControlsSlot extends GuiSlot {
    private Minecraft mc;
    IGuiControls parGui;
    CustomKeybinds keybinds;
    private int newButtonsAt = 0;
    public static class ButtonInfo {
        private GuiButton button;
        private String string;
        ButtonInfo(GuiButton button, String s) {
            this.button = button;
            this.string = s;
        }
        public String getString() {
            return string;
        }
        public GuiButton getButton() {
            return button;
        }
    }
    private LinkedList<ButtonInfo> buttons;
    private GuiControls guiControls;
    public GuiControlsSlot(IGuiControls gui, Minecraft mc, int i, int i1, int i2, int i3, int i4, List<GuiButton> list) {
        super(mc, i, i1, i2, i3, i4);
        keybinds = MegaMod.getCustomKeybinds();
        LinkedList<String> defaultStrings = new LinkedList<>();
        for(int p=0;p < mc.gameSettings.keyBindings.length;p++) {
            defaultStrings.addLast(mc.gameSettings.getKeyBindingDescription(p));
        }
        int o = 0;
        LinkedList<ButtonInfo> defaultButtons = new LinkedList<>();
        for(GuiButton button : list) {
            defaultButtons.addLast(new ButtonInfo(button, defaultStrings.get(o)));
            o++;
        }
        newButtonsAt = o;
        keybinds.keyCheck();
        for(Map.Entry<String, CustomKeybinds.SavedKey> entry : keybinds.getSavedKeys().entrySet()) {
            if(!keybinds.isKeyDisabled(entry.getKey())) {
                defaultButtons.addLast(new ButtonInfo(new GuiSmallButton(o, 0, 0, 70, 20, Keyboard.getKeyName(entry.getValue().getKey())), entry.getKey()));
                o++;
            }
        }
        buttons = defaultButtons;
        parGui = gui;
        this.mc = mc;
    }
    @Override
    protected int getSize() {
        return this.buttons.size();
    }

    @Override
    protected void elementClicked(int i, boolean b) {
        parGui.onAction(buttons.get(i).button, i >= this.newButtonsAt ? buttons.get(i) : null);
    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @Override
    protected void drawBackground() {
        parGui.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int i, int i1, int i2, int i3, Tessellator tessellator) {
        GuiButton button = buttons.get(i).button;
        button.xPosition = i1+150;
        button.yPosition = i2;
        button.drawButton(mc, i1+150, i2);
        String str = buttons.get(i).string;
        parGui.drawString(mc.fontRenderer, str, i1, i2, -1);
        if(CustomKeybinds.defaultKeybinds.containsKey(str)) {
            String key = Keyboard.getKeyName(CustomKeybinds.defaultKeybinds.get(str));
            parGui.drawString(mc.fontRenderer, "Default Key " + key, i1, i2+12, 0x525252);
        } else {
            parGui.drawString(mc.fontRenderer, "Default Key UNKNOWN", i1, i2+12, 0x525252);
        }
    }
}
