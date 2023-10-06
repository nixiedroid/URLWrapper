package com.nixiedroid.urlWrapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FilePreloader {
    public static void load(String res){
        try {
            InputStream i =  FilePreloader.class.getResourceAsStream(res);
            if (i != null) {
                BufferedReader r = new BufferedReader(new InputStreamReader(i));

                // reads each line
                String l;
                while((l = r.readLine()) != null) {
                    System.out.println(l);
                }
                i.close();
            }
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
}
