package net.oldhaven.devpack;

import net.oldhaven.MMDebug;

import java.util.HashMap;

public class Benchmark {
    public static final HashMap<String, Long> hashMap = new HashMap<>();
    public static void begin(String name) {
        hashMap.put(name, System.currentTimeMillis());
    }
    public static void end(String name) {
        long cur = System.currentTimeMillis();
        long old = hashMap.get(name);
        MMDebug.println("Benchmark for " + name + " in " + (cur - old) + "ms");
        hashMap.remove(name);
    }
}
