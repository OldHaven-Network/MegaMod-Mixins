package net.oldhaven.javascript.classes;

import net.oldhaven.javascript.JSEngine;
import org.mozilla.javascript.Function;

public class JSC_Controls {
    private JSEngine jsEngine;
    private String fileName;
    public JSC_Controls(JSEngine jsEngine, String fileName) {
        this.jsEngine = jsEngine;
        this.fileName = fileName;
    }
    public void createControl(String name, int key, Function function) {
        System.out.println("  [JS-Controls ("+fileName+")]: Creating new control of " + name);
        System.out.println(function.getClass().getName());
        jsEngine.getKeybinds().createKey(name, key, function);
    }
}
