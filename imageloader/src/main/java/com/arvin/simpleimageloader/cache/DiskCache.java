package com.arvin.simpleimageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.arvin.simpleimageloader.utils.CloseUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by arvin on 2016/10/27.
 */

public class DiskCache extends BitmapCache {
    private static final String BITMAP_CACHE = "com.arvin.imageloader/";
    private static final String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BITMAP_CACHE;

    public DiskCache() {
        File dir = new File(CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void put(String url, Bitmap b) {
        File f = new File(CACHE_DIR, url);
        FileOutputStream fos = null;
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
        }
    }


    @Override
    public Bitmap get(String url) {
        File f = new File(CACHE_DIR,url);
        if (!f.exists())
            return null;
        Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
        return b;
    }
}
