package net.oldhaven.customs;

import net.oldhaven.MegaMod;

public class ServerPacketInformation {
    public ServerPacketInformation(MegaMod megaMod) {}

    private boolean canFly = false;
    private boolean customChat = false;

    public void reset() {
        this.canFly = false;
        this.customChat = false;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
        if(!canFly)
            MegaMod.getInstance().isFlying = false;
    }
    public void setCustomChat(boolean customChat) {
        this.customChat = customChat;
    }

    public boolean isCustomChat() {
        return customChat;
    }
    public boolean canFly() {
        return canFly;
    }
}
