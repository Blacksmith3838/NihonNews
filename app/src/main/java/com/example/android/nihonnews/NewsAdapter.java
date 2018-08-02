package com.example.android.nihonnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
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
        TextView cardTitle = listItemView.findViewById(R.id.card_title);
        TextView cardAuthor = listItemView.findViewById(R.id.author);
        TextView cardDate = listItemView.findViewById(R.id.date);

        //get current position
        News currentNews = getItem(position);

        //set views
        cardTitle.setText(currentNews.getTitle());
        cardAuthor.setText(currentNews.getAuthor());
        //use date helper methods
        cardDate.setText(FormatDate(currentNews.getDate()));


        return listItemView;
    }

    private String FormatDate(String dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

        try {
            //turn date into actual date
            Date articleDate = dateFormat.parse(dateObject);

            //converts to readable date
            dateFormat.applyPattern("LLL dd, yyyy");;
            return dateFormat.format(articleDate);

        }catch (ParseException e) {
            Log.e("Adapter", "Problem formatting data.", e);
            return null;
        }

    }
}
