package excimer.nrepl;

import excimer.clojure.nrepl.ClojureNREPLServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A nREPL server.
 */
public class NREPLServer implements ApplicationContextAware {

    public static volatile NREPLServer INSTANCE = null;

    private ApplicationContext applicationContext;

    public NREPLServer() {
        this(0);
    }

    public NREPLServer(int portArg) {
        INSTANCE = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContextArg) throws BeansException {
        applicationContext = applicationContextArg;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Integer getPort() {
        return ClojureNREPLServer.getPort();
    }

    public void startServer() {
        startServer(0);
    }

    public void startServer(Integer port) {
        ClojureNREPLServer.startServer(port);
    }

    public void stopServer() {
        ClojureNREPLServer.stopServer();
    }
}