package net.oldhaven.javascript;

import org.mozilla.javascript.Function;

import java.util.HashMap;
import java.util.Map;

public class JSKeybinds {
    public JSEngine jsEngine;
    public JSKeybinds(JSEngine jsEngine) {
        this.jsEngine = jsEngine;
    }

    private static Map<String, JSKey> keys = new HashMap<>();
    public static class JSKey {
        public int key;
        public Function function;
        private JSKey(int key, Function consumer) {
            this.key = key;
            this.function = consumer;
        }
    }
    public void createKey(String name, int key, Function consumer) {
        if(keys.containsKey(name))
            jsEngine.console.error("NAME " + name + " ALREADY EXISTS FOR KEYBIND! Ignoring...");
        else
            keys.put(name, new JSKey(key, consumer));
    }
    public Map<String, JSKey> getKeys() {
        return keys;
    }
}
