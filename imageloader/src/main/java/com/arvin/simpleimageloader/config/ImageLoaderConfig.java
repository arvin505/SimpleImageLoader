package com.arvin.simpleimageloader.config;

import android.support.annotation.DrawableRes;

import com.arvin.simpleimageloader.cache.BitmapCache;
import com.arvin.simpleimageloader.cache.DoubleCache;
import com.arvin.simpleimageloader.cache.MemoryCache;

/**
 * Created by arvin on 2016/10/30.
 */

public class ImageLoaderConfig {
    //缓存
    public BitmapCache mCache = new DoubleCache();
    //加载中和加载失败显示配置
    public DisplayConfig mDisplayConfig = new DisplayConfig();

    public int threadCount = Runtime.getRuntime().availableProcessors() + 1;

    private ImageLoaderConfig() {

    }


    /**
     * 配置类builder
     */
    public static class Builder {
        BitmapCache bConfig = new MemoryCache();
        DisplayConfig bDisplayConfig = new DisplayConfig();
        int bThreadCount = Runtime.getRuntime().availableProcessors() + 1;

        public Builder setThreadCount(int threadCount) {
            bThreadCount = Math.max(1, threadCount);
            return this;
        }

        public Builder setCache(BitmapCache cache) {
            bConfig = cache;
            return this;
        }

        public Builder setLoadingPlaceholder(@DrawableRes int resId) {
            bDisplayConfig.loadingResId = resId;
            return this;
        }

        public Builder setNotFoundPlaceholder(@DrawableRes int resId) {
            bDisplayConfig.failedResId = resId;
            return this;
        }

        void applyConfig(ImageLoaderConfig confg) {
            confg.mCache = this.bConfig;
            confg.mDisplayConfig = this.bDisplayConfig;
            confg.threadCount = this.bThreadCount;
        }

        public ImageLoaderConfig create() {
            ImageLoaderConfig config = new ImageLoaderConfig();
            applyConfig(config);
            return config;
        }
    }
}
