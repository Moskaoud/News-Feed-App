package com.example.newsfeedapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    public static final String LOG_TAG = MainActivity.class.getName();
    final private  String NEWS_BASE_URL = "https://content.guardianapis.com/search";
    final private String API_KEY = "test";
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ListView mNewsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = findViewById(R.id.empty_state_tv);
        mNewsList = findViewById(R.id.list_news);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        mNewsList.setAdapter(mAdapter);
        mNewsList.setEmptyView(mEmptyStateTextView);

        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News current = mAdapter.getItem(i);
                Uri newsUri = Uri.parse(current.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(NEWS_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        ///you can type any query here and get right result
        uriBuilder.appendQueryParameter("q","USA");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("api-key",API_KEY );
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        mEmptyStateTextView.setText(R.string.no_news_found);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
