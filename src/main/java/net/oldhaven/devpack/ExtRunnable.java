package net.oldhaven.devpack;

@FunctionalInterface
public interface ExtRunnable<T> {
    void run(T type);
}

