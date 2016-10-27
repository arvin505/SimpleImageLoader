package com.arvin.simpleimageloader.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by arvin on 2016/10/27.
 */

public class CloseUtil {
    public static void close(Closeable c){
        try {
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
