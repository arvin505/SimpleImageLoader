package com.arvin.simpleimageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by arvin on 2016/10/27.
 */

public abstract class BitmapCache {
    public abstract void put(String url,Bitmap b);

    public abstract Bitmap get(String url);
}
