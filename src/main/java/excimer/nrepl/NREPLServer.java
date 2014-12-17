package excimer.nrepl;

import excimer.clojure.nrepl.ClojureNREPLServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * A nREPL server.
 */
@Component
@ManagedResource(objectName = "excimer:name=NREPLServer")
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

    @ManagedAttribute(description = "The port of the NREPLServer. Null if it is not running.")
    public Integer getPort() {
        return ClojureNREPLServer.getPort();
    }

    @ManagedAttribute(description = "Show if the NREPLServer is running or not.")
    public Boolean isRunning() {
        return ClojureNREPLServer.isRunning();
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