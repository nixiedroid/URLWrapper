package com.nixiedroid.urlWrapper;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.jar.JarFile;

public class Agent {
    private static final int JAVA_COMPILE_VERSION = 17;

    public static void premain(String args, Instrumentation inst) throws Exception {
        String agentPath = ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
                .filter(arg -> arg.startsWith("-javaagent"))
                .findAny().map(arg -> arg.substring(11))
                .orElseThrow(() -> new RuntimeException("Current javaagent jar path not found"));


        if (JavaVersionChecker.getVersion()<JAVA_COMPILE_VERSION){
            Logger.log.info("Detected too old java version: " + JavaVersionChecker.getVersion() );

            if (! "true".equals(System.getProperty("urlWrapper.javaVerIgnore", "false"))) {
                Logger.log.info("Found urlWrapper.javaVerIgnore = true. Ignoring old java version");
            } else {
              throw new RuntimeException("Shutting down due to old java version. Use -DurlWrapper.javaVerIgnore=true to override");
            }
        }
        Logger.log.info("Starting URL Hook");
        if ("false".equals(System.getProperty("urlWrapper.doRedirect", "false"))) {
                Logger.log.info("Logging urls only. Without redirect");
        }

        inst.appendToBootstrapClassLoaderSearch(new JarFile(agentPath));
        inst.addTransformer(new ServiceTransformer(), true);
        inst.retransformClasses(java.net.URL.class);
    }

}
