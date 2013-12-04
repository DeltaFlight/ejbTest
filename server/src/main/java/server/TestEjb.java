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
    private static Map<String, String> threads = new ConcurrentHashMap<>();

    @Override
    public String foo(String bar) {
        int cur = count.incrementAndGet();
        threads.put(Thread.currentThread().getName(), "");
        int maxVal = max.get();
        while (cur > maxVal && !max.compareAndSet(maxVal, cur)) {
            maxVal = max.get();
        }
//        for (int i = 0; i < 1; ++i) {
//            System.out.println("ZzzzzzXXX...");
//            ThreadUtils.sleep(1000);
//        }
        count.decrementAndGet();
        return bar.toUpperCase() + "XXX " + System.getProperty("jboss.node.name") + "\n";
    }
}
