package com.proprog.booklisting;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Book>>
        , SearchView.OnQueryTextListener {

    private final static int LOADER_ID = 0;
    private final String URL_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    private String fullUrl;
    private ArrayList<Book> books;
    private ListView listView;
    private BookListAdapter bookListAdapter;
    private final String LOG_MSG_CLASS = MainActivity.class.getSimpleName();
    private View empty;
    private View disconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String searchKey = "android";
        listView = (ListView) findViewById(R.id.book_list_view);
        empty = findViewById(R.id.empty);
        disconnected = findViewById(R.id.disconnected);
        doBookSearch(searchKey);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_book_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_book_icon);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new BookTaskLoader(getApplicationContext(), fullUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        Log.d(LOG_MSG_CLASS, "onLoadFinished");
        if (data != null) {
            books = new ArrayList<>();
            books.addAll(data);

            listView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            disconnected.setVisibility(View.GONE);

            bookListAdapter = new BookListAdapter(this, books);
            listView.setAdapter(bookListAdapter);
        } else {
            Log.d(LOG_MSG_CLASS, "onLoadFinished : No Data");
            listView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            disconnected.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        Log.d(LOG_MSG_CLASS, "onLoaderReset");
        if (bookListAdapter != null) {
            bookListAdapter.clear();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doBookSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //to show the search view is working properly
        doBookSearch(newText);
        return true;
    }

    public void doBookSearch(String key) {
        if (isConnectedToInternet()) {
            listView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            disconnected.setVisibility(View.GONE);

            Log.d(LOG_MSG_CLASS, "doBookSearch : " + key);
            String key_no_spaced = key.replaceAll(" ", "").toLowerCase();
            Log.d(LOG_MSG_CLASS, "doBookSearch : " + key_no_spaced);
            fullUrl = URL_QUERY + key_no_spaced;
            Log.d(LOG_MSG_CLASS, "doBookSearch : " + fullUrl);
            getSupportLoaderManager().destroyLoader(LOADER_ID);
            getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            listView.setVisibility(View.GONE);
            disconnected.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }

    public boolean isConnectedToInternet() {
        boolean netState = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            netState = true;
        }
        return netState;
    }

}
