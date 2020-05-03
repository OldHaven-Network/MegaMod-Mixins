package net.oldhaven.javascript;

import net.oldhaven.MegaMod;
import net.oldhaven.javascript.classes.JSC_Controls;
import net.oldhaven.javascript.classes.JSC_Mixins;

import javax.script.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.function.Consumer;

/**
 * @author cutezyash
 * @version JS-0.1
 * @apiNote Unfinished
 *
 * Want to implement this into your mod?
 * - First. GIVE ME CREDIT
 * - I didn't make this in a day. This was a large project
 * - Please give me credit if you use it, thanks!
 * - Copy all the files in src/javascript/ to your mod for src/javascript/
 * - Then, to start the engine just use new JSEngine(); in Minecraft.class
 * - Also, copy minecraft.jar/js/ into your mod.
 */
public class JSEngine {
    public String jsVersion = "0.1";
    private JSKeybinds keybinds;
    Console console;
    private ScriptEngine scriptEngine;
    private Bindings bindings;
    public JSEngine() {
        this.scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        this.console = new Console();
        this.keybinds = new JSKeybinds(this);
        console.fileName = "Root";
        this.bindings = scriptEngine.createBindings();
        this.createBindings(console.fileName);
        scriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        /*try {
            FileReader reader = new FileReader(getClass().getClassLoader().getResource("js/autoexec.js").getFile());
            scriptEngine.eval(new BufferedReader(reader));
        } catch (ScriptException | FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    private void createBindings(String name) {
        if(!bindings.containsKey("js")) {
            bindings.put("js", this);
            bindings.put("print", (Consumer<Object>) console::print);
            bindings.put("console", console);
            bindings.put("Minecraft", MegaMod.getMinecraftInstance());
            //bindings.put("Keyboard", StaticClass.forClass(org.lwjgl.input.Keyboard.class));
            bindings.put("Mixins", new JSC_Mixins());
        } else
            bindings.remove("Controls");
        bindings.put("Controls", new JSC_Controls(this, name));
    }

    public JSKeybinds getKeybinds() {
        return this.keybinds;
    }

    static class Console {
        private String fileName;
        public void error(String... args) {
            print(args, "__ERR");
        }
        public void log(String... args) {
            print((Object)args);
        }
        void print(Object... args) {
            if(args.length > 1) {
                if (args[1].equals("__ERR")) {
                    System.out.println("  [JS-ERROR (" + fileName + ")]: " + args[0]);
                    return;
                }
            }
            if(args.length < 1 || String.valueOf(args[0]).trim().isEmpty())
                System.out.println();
            else
                System.out.println("  [JS ("+fileName+")]: " + args[0]);
        }
    }


    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }
    public Bindings getBindings() {
        return bindings;
    }
}
