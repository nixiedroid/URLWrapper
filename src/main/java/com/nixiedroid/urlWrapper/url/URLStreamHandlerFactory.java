package com.nixiedroid.urlWrapper.url;

import com.nixiedroid.urlWrapper.LOG;

import java.net.URLStreamHandler;

public class URLStreamHandlerFactory implements java.net.URLStreamHandlerFactory {
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equals("https")) {
            LOG.i("Wrapping " + protocol + " requests");
            return new com.nixiedroid.urlWrapper.url.https.Handler();
        } else if (protocol.equals("http")) {
            LOG.i("Wrapping " + protocol + " requests");
            return new com.nixiedroid.urlWrapper.url.http.Handler();
        } else
            return null;
    }
}
