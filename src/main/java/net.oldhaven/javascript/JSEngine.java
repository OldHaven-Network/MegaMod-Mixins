package net.oldhaven.javascript;

import net.oldhaven.MegaMod;
import net.oldhaven.customs.util.MMUtil;
import net.oldhaven.javascript.classes.JSC_Controls;
import net.oldhaven.javascript.classes.JSC_Mixins;
import org.lwjgl.input.Keyboard;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <h1>IMPLEMENTATION FOR JS MODS</h1>
 * <p>
 *     This is the simplest, easiest way to
 *     make mod creations for MegaMod
 * </p>
 * @author cutezyash
 */
public class JSEngine {
    public String version = "0.1";
    private JSKeybinds keybinds;
    Console console;
    public JSEngine() {
        this.console = new Console("root");
        Context cx = Context.enter();
        Scriptable scope = cx.initSafeStandardObjects();
        this.keybinds = new JSKeybinds(this);
        this.createBindings(cx, scope, console.fileName);
        try {
            FileReader reader = new FileReader(getClass().getClassLoader().getResource("js/autoexec.js").getFile());
            cx.evaluateReader(scope, reader, console.fileName, 1, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Test {

    }

    private void createBindings(Context cx, Scriptable scope, String name) {
        if(!scope.has("JSEngine", scope)) {
            ScriptableObject.putProperty(scope,"JSEngine", Context.javaToJS(this, scope));
            ScriptableObject.putProperty(scope,"Server", Context.javaToJS(MMUtil.getServerPacketInformation(), scope));
            ScriptableObject.putProperty(scope,"player", Context.javaToJS(MMUtil.getPlayer(), scope));
            //ScriptableObject.putProperty(scope, "Keyboard", Context.javaToJS(Keyboard.class, scope));
            //ScriptableObject.putProperty(scope,"print", (Consumer<Object>) console::print);
            ScriptableObject.putProperty(scope,"console", Context.javaToJS(console, scope));
            ScriptableObject.putProperty(scope,"version", MegaMod.version);
            ScriptableObject.putProperty(scope,"Minecraft", Context.javaToJS(MMUtil.getMinecraftInstance(), scope));
            //bindings.put("Keyboard", StaticClass.forClass(org.lwjgl.input.Keyboard.class));
            ScriptableObject.putProperty(scope,"Mixins", Context.javaToJS(new JSC_Mixins(), scope));
        } else
            ScriptableObject.putProperty(scope, "Controls", null);
        ScriptableObject.putProperty(scope, "Controls", Context.javaToJS(new JSC_Controls(this, name), scope));
        StringBuilder keyboardClass = new StringBuilder("class Keyboard {");
        Field[] declaredFields = Keyboard.class.getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                    keyboardClass.append(field.getName()).append(" = ").append(field.getInt(null)).append(";");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        keyboardClass.append("}");
        //Test test = (Test) Context.jsToJava(keyboardClass.toString(), Test.class);
        //cx.evaluateString(scope, keyboardClass.toString(), console.fileName, 1, null);
    }

    public JSKeybinds getKeybinds() {
        return this.keybinds;
    }

    public static class Console {
        private String fileName;
        Console(String original) {
            this.fileName = original;
        }
        public void error(String... args) {
            print(args, "__ERR");
        }
        public void log(String... args) {
            print((Object)args);
        }
        public void print(Object... args) {
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
}
