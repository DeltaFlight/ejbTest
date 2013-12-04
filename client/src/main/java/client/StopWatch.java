package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static client.ConvertUtils.readableNumber;

/**
 * @author fly
 */
public class StopWatch {
    private static final Logger logger = LoggerFactory.getLogger(StopWatch.class);
    public static final double NANOS_IN_MILLI = 1_000_000.0;
    public static final double NANOS_IN_SECOND = 1_000_000_000.0;

    private long start = System.nanoTime();

    public long stop() {
        return Math.round(stopNano() / NANOS_IN_MILLI);
    }

    private long stopNano() {
        return System.nanoTime() - start;
    }

    public void stopAndLog(String message) {
        double elapsedSeconds = stopNano() / NANOS_IN_SECOND;
        logger.info("{} took {}s", message, readableNumber(elapsedSeconds));
    }

    public String stopAndLog(String message, long iterations) {
        return stopAndLog(message, iterations, 1);
    }

    public String stopAndLog(String message, long iterations, int threads) {
        double elapsedSeconds = stopNano() / NANOS_IN_SECOND;
        double rps = iterations / elapsedSeconds;
        double period = elapsedSeconds / iterations;
        final String msg;
        if (threads > 1) {
            msg = String.format("%s took %ss, %srps, avg latency %ss", message,
                                readableNumber(elapsedSeconds),
                                readableNumber(rps),
                                readableNumber(period * threads));
        } else {
            msg = String.format("%s took %ss, %srps, %ss per invocation", message,
                                readableNumber(elapsedSeconds),
                                readableNumber(rps),
                                readableNumber(period));
        }
        logger.info(msg);
        return msg;
    }

}
