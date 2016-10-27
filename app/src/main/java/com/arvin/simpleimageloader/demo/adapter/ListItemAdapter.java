package com.arvin.simpleimageloader.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.arvin.simpleimageloader.core.ImageLoader;
import com.arvin.simpleimageloader.demo.R;

/**
 * Created by arvin on 2016/10/27.
 */

public class ListItemAdapter extends ArrayAdapter<String> {


    public ListItemAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.item_img);
        ImageLoader.getInstance().displayImage(img,item);
        return convertView;
    }
}
