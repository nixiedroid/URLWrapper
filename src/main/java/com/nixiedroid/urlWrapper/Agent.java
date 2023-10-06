package com.nixiedroid.urlWrapper;

import com.nixiedroid.urlWrapper.url.URLStreamHandlerReset;
@SuppressWarnings("unused")
public class Agent {

    public static void premain(String args) {
        if (getJavaVersion() != 8) throw new RuntimeException("Java 8 Required");
        LOG.i("Doing Magic");
        URLStreamHandlerReset.reset();
        FilePreloader.load("/SampleFile.txt");
    }

    /**
     * Returns the Java version as an int value.
     * @return the Java version as an int value (8, 9, etc.)
     */
    private static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2);
        }
        int dotPos = version.indexOf('.');
        int dashPos = version.indexOf('-');
        return Integer.parseInt(version.substring(0,
                dotPos > -1 ? dotPos : dashPos > -1 ? dashPos : 1));
    }
}
