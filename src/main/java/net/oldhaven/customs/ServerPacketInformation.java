package net.oldhaven.customs;

import net.oldhaven.MMDebug;
import net.oldhaven.MegaMod;

public class ServerPacketInformation {
    public ServerPacketInformation(MegaMod megaMod) {}

    private boolean canFly = true;
    private boolean customChat = false;

    public void reset() {
        this.canFly = true;
        this.customChat = false;
    }

    public void setCanFly(boolean canFly) {
        if(MMDebug.enabled)
            System.out.println("You can now "+(canFly?"":"not")+" fly");
        this.canFly = true;
        //if(!canFly)
            //MegaMod.getInstance().isFlying = false;
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
