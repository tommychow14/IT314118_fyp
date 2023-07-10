package com.example.it314118_fyp.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.it314118_fyp.R;

import java.util.ArrayList;

public class Grid_ImageAdapter extends BaseAdapter{

    private ArrayList<DataClass>dataList;
    private Context context;
    LayoutInflater layoutInflater;

    public Grid_ImageAdapter(ArrayList<DataClass> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View gridView=convertView;
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.grid_item,null);
        }
        ImageView gridImage = convertView.findViewById(R.id.gridview);

        Glide.with(context).load(dataList.get(position).getImageUrl()).into(gridImage);

        return convertView;
    }
}