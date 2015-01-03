package excimer.clojure.btrace.compile;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BtraceCompile {
    private static final Logger LOGGER = LoggerFactory.getLogger(BtraceCompile.class);

    private static final String EXCIMER_BTRACE_COMPILE_NS = "excimer.btrace.compile";

    private static final IFn REQUIRE;
    static {
        REQUIRE = Clojure.var("clojure.core", "require");
        REQUIRE.invoke(Clojure.read(EXCIMER_BTRACE_COMPILE_NS));
    }

    private static final IFn BTRACE_COMPILE = Clojure.var(EXCIMER_BTRACE_COMPILE_NS, "btrace-compile");

    private static final IFn CREATE_BTRACE_CLIENT = Clojure.var(EXCIMER_BTRACE_COMPILE_NS, "create-btrace-client");

    public static byte[] compile(String javaPath) {
        final Object btraceInst = CREATE_BTRACE_CLIENT.invoke();
        final byte[] bytecode = (byte[]) BTRACE_COMPILE.invoke(btraceInst, javaPath);
        return bytecode;
    }
}
