package com.example.nikos.unipiapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends ArrayAdapter<NewsDataModel> {
    ArrayList<NewsDataModel> news;
    Context context;
    int resource;

    public NewsListAdapter(Context context, int resource, ArrayList<NewsDataModel> news) {
        super(context, resource, news);
        this.news = news;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_layout, null, true);

        }
        NewsDataModel news = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgNewsPicture);
        Picasso.with(context).load(news.getArticles().get(position).getUrlToImage()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtTitle);
        txtName.setText(news.getArticles().get(position).getTitle());


        return convertView;
    }
}


