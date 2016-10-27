package com.arvin.simpleimageloader.cache;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.LruCache;

/**
 * Created by arvin on 2016/10/27.
 */

public class MemoryCache extends BitmapCache {
    LruCache<String, Bitmap> mImageCache;

    public MemoryCache() {
        initMemoryCache();
    }

    private void initMemoryCache() {
        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemorySize / 8;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight() * value.getRowBytes() / 1024;
            }
        };
    }


    @Override
    public void put(String url, Bitmap b) {
        mImageCache.put(url, b);
    }

    @Override
    public Bitmap get(String url) {
        return mImageCache.get(url);
    }
}
