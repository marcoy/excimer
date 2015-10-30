package excimer.nrepl;

import excimer.clojure.nrepl.ClojureNREPLServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

/**
 * A nREPL server.
 */
@Component
@ManagedResource(objectName = "excimer:name=NREPLServer")
public class NREPLServer implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(NREPLServer.class);

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

    @ManagedOperation(description = "Start the NREPLServer on a randomly chosen port.")
    public void startServer() {
        startServer(0);
    }

    @ManagedOperation(description = "Start the NREPLServer on the given port")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "port", description = "Port to listen on.")
    })
    public void startServer(Integer port) {
        LOGGER.info("Starting the nREPL server on port {}", port);
        ClojureNREPLServer.startServer(port);
    }

    @ManagedOperation(description = "Stop the NREPLServer.")
    public void stopServer() {
        LOGGER.info("Stopping the nREPL server");
        ClojureNREPLServer.stopServer();
    }
}
