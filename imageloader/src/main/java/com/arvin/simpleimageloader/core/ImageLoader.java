package com.arvin.simpleimageloader.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.arvin.simpleimageloader.cache.BitmapCache;
import com.arvin.simpleimageloader.cache.DoubleCache;
import com.arvin.simpleimageloader.config.DisplayConfig;
import com.arvin.simpleimageloader.config.ImageLoaderConfig;
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

    ExecutorService mExcutorService;

    private ImageLoaderConfig mConfig;

    private int mThreadCount = Runtime.getRuntime().availableProcessors() + 2;

    private BitmapCache mImageCache;

    private DisplayConfig mDisplayConfig;

    private ImageLoader() {
    }

    private static class ImageLoaderHolder {
        private static ImageLoader sInstance = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.sInstance;
    }


    public void init(ImageLoaderConfig config) {
        mConfig = config;
        checkConfig();
    }

    private void checkConfig() {
        if (mConfig == null)
            throw new RuntimeException(
                    "The config of SimpleImageLoader is Null, please call the init(ImageLoaderConfig config) method to initialize");
        mImageCache = mConfig.mCache;
        mThreadCount = mConfig.threadCount;
        mDisplayConfig = mConfig.mDisplayConfig;
        mExcutorService = Executors.newFixedThreadPool(mThreadCount);
    }


    public void displayImage(ImageView imageView, String url) {
        String cacheUrl = filterUrl(url);
        Bitmap b = mImageCache.get(cacheUrl);
        if (b != null) {
            imageView.setImageBitmap(b);
        } else {
            // FIXME: 2016/10/27 下载
            imageView.setTag(cacheUrl);
            imageView.setImageResource(mDisplayConfig.loadingResId);
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
                    Map<ImageView, Bitmap> data = new HashMap<ImageView, Bitmap>();
                    data.put(imageView, b);
                    msg.obj = data;
                    if (imageView.getTag().equals(filterUrl(imageUrl))) {
                        mH.sendMessage(msg);
                    }
                    mImageCache.put(filterUrl(imageUrl), b);
                    conn.disconnect();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(is);
                }

                Message msg = mH.obtainMessage();
                Map<ImageView, Bitmap> data = new HashMap<ImageView, Bitmap>();
                data.put(imageView, null);
                msg.obj = data;
                if (imageView.getTag().equals(filterUrl(imageUrl))) {
                    mH.sendMessage(msg);
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

    private static Handler mH = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Map<ImageView, Bitmap> data = (Map<ImageView, Bitmap>) msg.obj;
            for (Map.Entry<ImageView, Bitmap> entry : data.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getKey().setImageBitmap(entry.getValue());
                } else {
                    entry.getKey().setImageResource(ImageLoader.getInstance().mDisplayConfig.failedResId);
                }

            }
        }
    };
}
