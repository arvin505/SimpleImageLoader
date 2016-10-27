package com.arvin.simpleimageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by arvin on 2016/10/27.
 */

public class DoubleCache extends BitmapCache {
    DiskCache mDiskCache;
    MemoryCache mMemoryCache ;

    public DoubleCache() {
        mDiskCache = new DiskCache();
        mMemoryCache = new MemoryCache();
    }

    @Override
    public void put(String url, Bitmap b) {
        mMemoryCache.put(url,b);
        mDiskCache.put(url,b);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap b = mMemoryCache.get(url);
        if (b==null){
            b = mDiskCache.get(url);
        }
        return b;
    }
}
