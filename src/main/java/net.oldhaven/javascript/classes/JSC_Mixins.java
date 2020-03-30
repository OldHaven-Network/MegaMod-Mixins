package net.oldhaven.javascript.classes;

public class JSC_Mixins {
    public void createMixin(Class<? extends Class> clazz) {
        System.out.println(clazz.getName());
    }
}
