package com.nixiedroid.urlWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

public class URLPatcher {


    static {
        patterns = new ArrayList<>();
        load(new File("URLWrapper.urls"));
    }

    private static final ArrayList<Pattern> patterns;


    private URLPatcher() {
    }

    public static void load(String... config) {
        if (config.length % 2 != 0) return;
        for (int i = 0; i < config.length; i += 2) {
            patterns.add(new Pattern(config[i], config[i + 1]));
        }
    }

    public static void load(File configFile) {
        if (configFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(configFile.toPath())))) {
                String[] config = reader.lines()
                        .map(String::trim)
                        .filter(line -> line.length() > 2 && line.charAt(0) != '#')
                        .toArray(String[]::new);
                load(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static URL replace(URL url) {
        String urlString = url.toString();
        try {
            for (Pattern p : patterns) {
                if (urlString.startsWith(p.source)) {
                    return new URL(p.destination + urlString.substring(p.source.length()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class Pattern {
        final String source;
        final String destination;

        public Pattern(String source, String destination) {
            this.source = source;
            this.destination = destination;
        }
    }
}

