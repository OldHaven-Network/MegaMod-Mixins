package net.oldhaven.customs.packets;

import javax.annotation.Nonnull;

public interface IPacket {
    void onRun(String[] args);
    void send(String... args);
    @Nonnull String getName();
}
