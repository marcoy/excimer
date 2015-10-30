package excimer.clojure.nrepl;

import clojure.java.api.Clojure;
import clojure.lang.Atom;
import clojure.lang.IFn;

/**
 * A Java interface to excimer.nrepl.server.
 */
public class ClojureNREPLServer {
    private static final String EXCIMER_NREPL_NS = "excimer.nrepl.server";

    private static final IFn DEREF = Clojure.var("clojure.core", "deref");
    private static final IFn REQUIRE;
    static {
        REQUIRE = Clojure.var("clojure.core", "require");
        REQUIRE.invoke(Clojure.read(EXCIMER_NREPL_NS));
    }

    private static final IFn NREPL_SERVER = Clojure.var(EXCIMER_NREPL_NS, "nrepl-server");
    private static final IFn START_NREPL_SERVER = Clojure.var(EXCIMER_NREPL_NS, "start-nrepl-server");
    private static final IFn STOP_NREPL_SERVER  = Clojure.var(EXCIMER_NREPL_NS, "stop-nrepl-server");
    private static final IFn GET_PORT  = Clojure.var(EXCIMER_NREPL_NS, "get-port");
    private static final IFn IS_RUNNING  = Clojure.var(EXCIMER_NREPL_NS, "is-running");

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
     * @return The port number or -1 if nREPL server is not running.
     */
    public static Integer getPort() {
        return (Integer) GET_PORT.invoke();
    }

    /**
     * Check if a nREPL server is running.
     *
     * @return True is the server is running. Otherwise, false.
     */
    public static Boolean isRunning() {
        return (Boolean) IS_RUNNING.invoke();
    }

    /**
     * Return the nrepl-server atom.
     *
     * @return Atom that holds the NREPLServer.
     */
    public static Atom getNREPLServerAtom() {
        return (Atom) DEREF.invoke(NREPL_SERVER);
    }
}
