package net.oldhaven.javascript.classes;

import net.oldhaven.javascript.JSEngine;

import java.util.function.Consumer;

public class JSC_Controls {
    private JSEngine jsEngine;
    private String fileName;
    public JSC_Controls(JSEngine jsEngine, String fileName) {
        this.jsEngine = jsEngine;
        this.fileName = fileName;
    }
    public void createControl(String name, int key, Consumer<String> function) {
        System.out.println("  [JS-Controls ("+fileName+")]: Creating new control of " + name);
        jsEngine.getKeybinds().createKey(name, key, function);
    }
}
