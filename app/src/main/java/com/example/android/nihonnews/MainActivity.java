package com.example.android.nihonnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    private static final String Guardian_URL =
            "https://content.guardianapis.com/search?from-date=2018-01-01&to-date=2018-07-30&show-tags=contributor&page-size=10&q=japan&api-key=ad3b013e-b9ca-47c1-b3d1-b48761d76152";

    public static final String LOG_TAG = MainActivity.class.getName();
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;
    private static final int NEWS_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.spin_progress);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(LOG_TAG, "IT CONNECTED");
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            //init loader
            loaderManager.initLoader(NEWS_LOADER_ID, null, this).forceLoad();
        } else {
            mProgressBar = (ProgressBar) findViewById(R.id.spin_progress);
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
        }




        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);
        articleListView.setEmptyView(mEmptyStateTextView);


        // Create a new {@link ArrayAdapter} of News
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        //set onclick on the listView
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current News that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri NewsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(MainActivity.this, Guardian_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> result) {
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_News);
        // Clear the adapter of previous News data
        mAdapter.clear();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (result != null && !result.isEmpty()) {
           mAdapter.addAll(result);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
    }
}

