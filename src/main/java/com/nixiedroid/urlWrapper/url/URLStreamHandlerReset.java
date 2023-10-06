package com.nixiedroid.urlWrapper.url;

import java.lang.reflect.Field;
import java.net.URL;

public class URLStreamHandlerReset {
    private static void unsetURLStreamHandlerFactory() {
        try {
            Field e = URL.class.getDeclaredField("factory");
            e.setAccessible(true);
            e.set(null, null);
            URL.setURLStreamHandlerFactory(null);
        } catch (Exception ignored) {
        }
    }

    public static void reset() {
        unsetURLStreamHandlerFactory();
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory());
    }

}
