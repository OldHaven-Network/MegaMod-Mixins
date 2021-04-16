package net.oldhaven.devpack;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChunkExecutor {
    public static Executor executor = Executors.newCachedThreadPool();
}
