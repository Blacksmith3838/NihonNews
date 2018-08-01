package com.example.android.nihonnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> NewsList) {
        super(context, 0, NewsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_card, parent, false);
        }


        //find views
        TextView cardTitle = convertView.findViewById(R.id.card_title);
        TextView cardAuthor = convertView.findViewById(R.id.author);
        TextView cardDate = convertView.findViewById(R.id.date);

        //get current position
        News currentNews = getItem(position);
        Date dateObject = new Date(currentNews.getDate());

        //set views
        cardTitle.setText(currentNews.getTitle());
        cardAuthor.setText(currentNews.getAuthor());
        //use date helper methods
        String formattedDate = FormatDate(dateObject);
        cardDate.setText(formattedDate);


        return listItemView;
    }

    private String FormatDate(Date dateObject) {
        SimpleDateFormat DateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return DateFormat.format(dateObject);
    }
}
