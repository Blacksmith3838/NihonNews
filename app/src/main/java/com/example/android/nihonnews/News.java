package com.example.android.nihonnews;

public class News {
    private String mTitle, mAuthor, mDate, mUrl;

    public News (String Title, String Author, String Date, String Url){
        mTitle = Title;
        mAuthor = Author;
        mDate = Date;
        mUrl = Url;
    }

    //getter and return methods

    public String getTitle() { return mTitle; }

    public String getAuthor() { return mAuthor; }

    public String getDate() { return mDate; }

    public String getUrl() { return mUrl;}
}
