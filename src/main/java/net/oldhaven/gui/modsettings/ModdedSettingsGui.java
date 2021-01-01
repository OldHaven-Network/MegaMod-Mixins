package net.oldhaven.gui.modsettings;

import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.gui.CustomGuiButton;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class ModdedSettingsGui extends GuiScreen {
    boolean hasChanged = false;
    public List<GuiButton> controlList;
    public GuiScreen parentScreen;
    public GameSettings gameSettings;

    public ModdedSettingsGui(GuiScreen parentScreen, GameSettings gameSettings) {
        this.controlList = new ArrayList<>();
        this.parentScreen = parentScreen;
        this.gameSettings = gameSettings;
    }

    public abstract @Nonnull String getModSection();
    public abstract @Nonnull String getTitle();

    public final void setChanged() {
        hasChanged = true;
    }

    public int initGui(int i) {
        ModOptions.Section section = ModOptions.getSectionByName(getModSection());
        if(section == null) {
            System.err.println("INVALID MOD SECTION " + getModSection());
            return i;
        }
        for(ModOptions enumOption : section.getList()) {
            if(enumOption.isDisabled)
                continue;
            String option = enumOption.getName();
            if (enumOption.getStyle() == ModOptions.Style.FLOAT) {
                Float value = MMUtil.getCustomGameSettings().getOptionF(enumOption.getName());
                controlList.add(new CustomGuiButton.GuiSlider(
                        enumOption.getOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumOption, option, value == null ? 0F : value).setModdedGui(this));
            } else {
                controlList.add(new CustomGuiButton.GuiSmallButton(
                        enumOption.getOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumOption, option).setModdedGui(this));
            }
            i++;
        }
        super.controlList = controlList;
        return i;
    }

    private ModOptions lastHovering = null;
    public final void onButtonHover(ModOptions modOptions) {
        this.lastHovering = modOptions;
    }

    final void addDone() {
        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, StringTranslate.getInstance().translateKey("gui.done")));
        super.controlList = controlList;
    }

    @Override
    public void drawScreen(int i, int i1, float v) {
        super.drawScreen(i, i1, v);
        if(lastHovering != null) {
            String desc = lastHovering.getDescription();
            if(desc.isEmpty())
                desc = "No desc for " + lastHovering.getName();
            drawCenteredString(fontRenderer, desc, width / 2, 25, 0xffffff);
            lastHovering = null;
        }
        drawCenteredString(fontRenderer, getTitle(), width / 2, 10, 0xffffff);
    }
}
