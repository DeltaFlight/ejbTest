package client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author fly
 */
public class ThreadUtils {

    public static void sleep(long millis) {
        if (millis < 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdownAndWaitForever(ExecutorService threadPool) {
        shutdownAndWait(threadPool, 100500L, TimeUnit.DAYS);
    }

    public static void shutdownAndWait(ExecutorService threadPool, long timeout, TimeUnit unit) {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
