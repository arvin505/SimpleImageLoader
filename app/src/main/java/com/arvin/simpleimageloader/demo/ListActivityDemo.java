package com.arvin.simpleimageloader.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.arvin.simpleimageloader.demo.adapter.ListItemAdapter;

/**
 * Created by arvin on 2016/10/27.
 */
public class ListActivityDemo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView lv = (ListView) findViewById(R.id.lv);
        ListItemAdapter adapter = new ListItemAdapter(this,R.layout.list_item,Constant.urls);
        lv.setAdapter(adapter);
    }
}
