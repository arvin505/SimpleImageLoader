package com.arvin.simpleimageloader.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.arvin.simpleimageloader.cache.BitmapCache;
import com.arvin.simpleimageloader.cache.DiskCache;
import com.arvin.simpleimageloader.utils.CloseUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by arvin on 2016/10/27.
 */

public class ImageLoader {

    ExecutorService mExcutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 2);
    private BitmapCache mImageCache = new DiskCache();
    private ImageLoader() {
    }

    private static class ImageLoaderHolder {
        private static ImageLoader sInstance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.sInstance;
    }



    public void displayImage(ImageView imageView, String url) {
        String cacheUrl = filterUrl(url);
        Bitmap b = mImageCache.get(cacheUrl);
        if (b != null) {
            imageView.setImageBitmap(b);
        } else {
            // FIXME: 2016/10/27 下载
            imageView.setTag(cacheUrl);
            downloadBitmap(imageView, url);

        }

    }

    private void downloadBitmap(final ImageView imageView, final String imageUrl) {
        mExcutorService.submit(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    is = conn.getInputStream();
                    Bitmap b = BitmapFactory.decodeStream(is);
                    Message msg = mH.obtainMessage();
                    Map<ImageView , Bitmap> data = new HashMap<ImageView, Bitmap>();
                    data.put(imageView,b);
                    msg.obj = data;
                    if (imageView.getTag().equals(filterUrl(imageUrl))){
                        mH.sendMessage(msg);
                    }
                    mImageCache.put(filterUrl(imageUrl),b);
                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(is);
                }
            }
        });
    }

    private String filterUrl(String url) {
        if (TextUtils.isEmpty(url.trim())) {
            return null;
        }
        return url.replaceAll("/", "");
    }

    private static Handler mH = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<ImageView,Bitmap> data = (Map<ImageView, Bitmap>) msg.obj;
            for (Map.Entry<ImageView,Bitmap> entry : data.entrySet()){
                entry.getKey().setImageBitmap(entry.getValue());
            }
        }
    };
}
