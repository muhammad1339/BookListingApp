package com.proprog.booklisting;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamedAHMED on 2017-11-10.
 */

public class BookTaskLoader extends AsyncTaskLoader<ArrayList<Book>> {
    String mUrl;
    public BookTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        QueryUtils queryUtils = new QueryUtils();
        ArrayList<Book> books = queryUtils.fetchBookData(mUrl);
        return books;
    }

}