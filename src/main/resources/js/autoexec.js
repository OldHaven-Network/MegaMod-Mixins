function main() {
    print(" ");
    print("MegaMod: v" + Minecraft.mmVersion);
    print("MegaMod JS Engine: v" + js.jsVersion + " has started!");
    print(" ");
    //Controls.createControl("Fly", Keyboard.KEY_R, control_fly);
}


var isFlying = false;
function control_fly() {
    //Minecraft.thePlayer.isFlying = !Minecraft.thePlayer.isFlying;
}

main();