function print(str) { console.print(str); }

function main() {
    var version = JSEngine.version;
    print(" ");
    print("MegaMod: v" + version);
    print("MegaMod JS Engine: v" + version + " has started!");
    print(" ");
    //print(Controls);
    print(Keyboard);
    Controls.createControl("Fly", Keyboard.KEY_R, control_fly);
}
class test {
    EVENT_SIZE = 18;
    CHAR_NONE = 0;
}
var isFlying = false;
function control_fly() {
    Minecraft.thePlayer.isFlying = !Minecraft.thePlayer.isFlying;
}

main();