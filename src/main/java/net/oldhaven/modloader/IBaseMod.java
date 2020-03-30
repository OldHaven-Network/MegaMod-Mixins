package net.oldhaven.modloader;

import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.Random;

public interface IBaseMod {
    int AddFuel(int id);

    void ModsLoaded();

    boolean OnTickInGame(Minecraft game);

    String toString();

    String Version();
}
