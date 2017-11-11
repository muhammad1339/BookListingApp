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
    private final static int NO_DATA_ID = 1;
    private final static int NO_NETWORK_ID = 2;
    private final static int GOOD_ID = 3;
    private final static int LOADING_ID = 4;

    private final static String URL_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    private String fullUrl;
    private ArrayList<Book> books;
    private ListView listView;
    private BookListAdapter bookListAdapter;
    private final String LOG_MSG_CLASS = MainActivity.class.getSimpleName();
    private View empty;
    private View disconnected;
    private View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String searchKey = "android";
        listView = (ListView) findViewById(R.id.book_list_view);
        empty = findViewById(R.id.empty);
        disconnected = findViewById(R.id.disconnected);
        loading = findViewById(R.id.loading);
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
        handleStates(LOADING_ID);

        return new BookTaskLoader(getApplicationContext(), fullUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        Log.d(LOG_MSG_CLASS, "onLoadFinished");

        if (data != null && QueryUtils.IS_URL_GOOD) {
            books = new ArrayList<>();
            books.addAll(data);

            handleStates(GOOD_ID);

            bookListAdapter = new BookListAdapter(this, books);
            listView.setAdapter(bookListAdapter);
        } else {
            Log.d(LOG_MSG_CLASS, "onLoadFinished : No Data");
            handleStates(NO_DATA_ID);
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
        return false;
    }

    public void doBookSearch(String key) {
        if (isConnectedToInternet()) {
            handleStates(GOOD_ID);
            String key_no_spaced = key.replaceAll(" ", "").toLowerCase();
            fullUrl = URL_QUERY + key_no_spaced;

            Log.d(LOG_MSG_CLASS, "doBookSearch : " + key_no_spaced);
            Log.d(LOG_MSG_CLASS, "doBookSearch : " + fullUrl);
            getSupportLoaderManager().destroyLoader(LOADER_ID);

            getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            handleStates(NO_NETWORK_ID);
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

    public void handleStates(int stateId) {
        switch (stateId) {
            case NO_DATA_ID:
                listView.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                disconnected.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
            case NO_NETWORK_ID:
                listView.setVisibility(View.GONE);
                disconnected.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
            case LOADING_ID:
                listView.setVisibility(View.GONE);
                disconnected.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                break;
            default:
                listView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                disconnected.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
        }
    }
}
