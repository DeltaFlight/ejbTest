package client;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(JaxRs.PATH)
public class JaxRs extends Application {
    public static final String PATH = "/rest";
}
