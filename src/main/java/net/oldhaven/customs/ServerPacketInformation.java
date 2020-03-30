package net.oldhaven.customs;

import net.oldhaven.MegaMod;

public class ServerPacketInformation {
    public ServerPacketInformation(MegaMod megaMod) {}

    private boolean canFly = false;
    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }
    public boolean canFly() {
        return canFly;
    }
}
