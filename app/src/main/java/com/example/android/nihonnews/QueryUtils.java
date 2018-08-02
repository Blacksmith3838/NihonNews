package com.example.android.nihonnews;

import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //create URL
        URL url = createUrl(requestUrl);

        //perform http request
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        //extract data
        List<News> articleList = extractNews(jsonResponse);

        return articleList;
    }

    private static List<News> extractNews(String jsonResponse) {
        String title;
        String author;
        String date;
        String newsurl;
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<News> articleList = new ArrayList<>();
        try {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);
            JSONObject resultOfJSON = baseJSONResponse.getJSONObject("response");
            JSONArray arrayOfArticles = resultOfJSON.getJSONArray("results");

            //for loop extracting info
            for (int i = 0; i < arrayOfArticles.length(); i++) {

                JSONObject currentArticle = arrayOfArticles.getJSONObject(i);
                title = currentArticle.getString("webTitle");
                date = currentArticle.getString("webPublicationDate");
                newsurl = currentArticle.getString("webUrl");


                JSONArray authorArray = currentArticle.getJSONArray("tags");
                author = "";

                //check for author
                if (!authorArray.isNull(0)){
                    JSONObject currentTag = authorArray.getJSONObject(0);
                    author = currentTag.getString("webTitle");
                }

                //create new News object
                News news = new News(title, author, date, newsurl);

                //add News to list of articleList
                articleList.add(news);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem reading data", e);
        } return articleList;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //check for null
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        //create connection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't retrieve json");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return jsonResponse;
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();

            }

        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL", e);
        }
        return url;
    }
}
