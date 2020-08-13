package net.oldhaven.gui.modsettings;

import net.minecraft.src.GuiScreen;
import net.oldhaven.MegaMod;
import net.oldhaven.customs.options.ModOptions;
import net.oldhaven.gui.CustomGuiButton;

abstract class ModdedSettingsGui extends GuiScreen {
    public abstract String getModSection();

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
                Float value = MegaMod.getCustomGameSettings().getOptionF(enumOption.getName());
                controlList.add(new CustomGuiButton.GuiSlider(
                        enumOption.getOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumOption, option, value == null ? 0F : value));
            } else {
                controlList.add(new CustomGuiButton.GuiSmallButton(
                        enumOption.getOrdinal(),
                        (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1),
                        enumOption, option));
            }
            i++;
        }
        return i;
    }
}
