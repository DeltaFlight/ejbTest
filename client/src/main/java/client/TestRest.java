package client;

import com.google.common.base.Preconditions;
import iface.TestRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fly
 */
@Path("/test")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
@Singleton
public class TestRest {
    private static final Logger logger = LoggerFactory.getLogger(TestRest.class);

    @EJB(lookup = "ejb:/server/SampleBean!iface.TestRemote")
    private TestRemote sampleRemote;

    @GET
    @Produces("text/plain")
    @Path("/perf/{threads}/{iter}")
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public String perf(@PathParam("threads") final int threads, @PathParam("iter") final int iter) {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        StopWatch sw = new StopWatch();
        final AtomicLong errorCount = new AtomicLong();
        for (int j = 0; j < threads; ++j) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < iter / threads; ++i) {
                        try {
                            remote("test");
                        } catch (Throwable ex) {
                            errorCount.incrementAndGet();
                            logger.error("Error invoking EJB:", ex.getMessage(), ex);
                        }
                    }
                }
            });
        }
        ThreadUtils.shutdownAndWaitForever(pool);
        return sw.stopAndLog("Inv", iter, threads) + ", error count " + errorCount.get() + "\n";
    }

    private String remote(String in) {
        String foo = sampleRemote.foo(in);
        Preconditions.checkNotNull(foo);
        return foo;
    }
}
