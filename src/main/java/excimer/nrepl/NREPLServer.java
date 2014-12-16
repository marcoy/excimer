package excimer.nrepl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A nREPL server.
 */
public class NREPLServer implements ApplicationContextAware {
    public static volatile NREPLServer INSTANCE = null;

    private ApplicationContext applicationContext;

    private final int port;

    public NREPLServer() {
        this(0);
    }

    public NREPLServer(int portArg) {
        port = portArg;
        INSTANCE = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContextArg) throws BeansException {
        applicationContext = applicationContextArg;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}