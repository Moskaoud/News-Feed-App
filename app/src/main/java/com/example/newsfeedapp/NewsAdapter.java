package com.example.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, ArrayList<News> data) {
        super(context,0, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.news,null);
        }
        TextView date = view.findViewById(R.id.date);
        TextView title = view.findViewById(R.id.title);
        TextView url = view.findViewById(R.id.url);
        TextView author = view.findViewById(R.id.author);


        date.setText(getItem(position).getWebPublicationDate());
        title.setText(getItem(position).getWebTitle());
        url.setText(getItem(position).getWebUrl());
        author.setText(getItem(position).getAuthor());

        return  view;
    }
}
