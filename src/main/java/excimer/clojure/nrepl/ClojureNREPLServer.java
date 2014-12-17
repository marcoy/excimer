package excimer.clojure.nrepl;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

/**
 * A Java interface to excimer.nrepl.server.
 */
public class ClojureNREPLServer {
    private static final String EXCIMER_NREPL_NS = "excimer.nrepl.server";

    private static final IFn REQUIRE;
    static {
        REQUIRE = Clojure.var("clojure.core", "require");
        REQUIRE.invoke(Clojure.read(EXCIMER_NREPL_NS));
    }

    private static final IFn NREPL_SERVER = Clojure.var("excimer.nrepl.server", "nrepl-server");
    private static final IFn START_NREPL_SERVER = Clojure.var("excimer.nrepl.server", "start-nrepl-server");
    private static final IFn STOP_NREPL_SERVER  = Clojure.var("excimer.nrepl.server", "stop-nrepl-server");
    private static final IFn GET_PORT  = Clojure.var("excimer.nrepl.server", "get-port");

    public ClojureNREPLServer() {
    }

    /**
     * Start the nREPL server.
     */
    public static void startServer(Integer port) {
        START_NREPL_SERVER.invoke(port);
    }

    /**
     * Stop the nREPL server.
     */
    public static void stopServer() {
        STOP_NREPL_SERVER.invoke();
    }

    /**
     * Get the port that the nREPL server is listening on.
     *
     * @return The port number or null if nREPL server is not running.
     */
    public static Integer getPort() {
        return (Integer) GET_PORT.invoke();
    }
}
