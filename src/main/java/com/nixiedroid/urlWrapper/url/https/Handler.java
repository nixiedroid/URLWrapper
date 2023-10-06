package com.nixiedroid.urlWrapper.url.https;

import com.nixiedroid.urlWrapper.LOG;
import com.nixiedroid.urlWrapper.URLPatcher;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class Handler extends sun.net.www.protocol.https.Handler {
    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        URL newUrl = URLPatcher.replace(u);
        if (newUrl != null) {
            LOG.i("Replacing " + u + " with " + newUrl);
            return super.openConnection(newUrl, p);
        }
        return super.openConnection(u, p);
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return openConnection(u, null);
    }
}
