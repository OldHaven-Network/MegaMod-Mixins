package net.oldhaven.devpack;

@FunctionalInterface
public interface SingleCallback<T> {
    void run(T t);
}
