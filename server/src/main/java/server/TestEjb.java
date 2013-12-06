package server;

import iface.TestRemote;
import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fly
 */
@Stateless(name = "SampleBean")
@Remote(TestRemote.class)
@Clustered
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class TestEjb implements TestRemote {
    private static AtomicInteger count = new AtomicInteger();
    private static AtomicInteger max = new AtomicInteger();

    @Override
    public String foo(String bar) {
        int cur = count.incrementAndGet();
        int maxVal = max.get();
        while (cur > maxVal && !max.compareAndSet(maxVal, cur)) {
            maxVal = max.get();
        }
        count.decrementAndGet();
        return bar + " " + max + "\n";
    }

}
