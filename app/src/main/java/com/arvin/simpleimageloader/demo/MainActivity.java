package com.arvin.simpleimageloader.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.arvin.simpleimageloader.cache.DoubleCache;
import com.arvin.simpleimageloader.config.ImageLoaderConfig;
import com.arvin.simpleimageloader.core.ImageLoader;


public class MainActivity extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);

        ImageLoaderConfig.Builder builder = new ImageLoaderConfig.Builder();
        builder.setCache(new DoubleCache())
                .setThreadCount(4)
                .setLoadingPlaceholder(R.mipmap.ic_launcher)
                .setNotFoundPlaceholder(R.mipmap.ic_launcher);
        ImageLoader.getInstance().init(builder.create());

        ImageLoader.getInstance().displayImage(img, "http://himg.baidu.com/sys/portraitm/item/d62d6725?t=1474126779");
    }

    public void listView(View view) {
        Intent i = new Intent(this, ListActivityDemo.class);
        startActivity(i);
    }
}
