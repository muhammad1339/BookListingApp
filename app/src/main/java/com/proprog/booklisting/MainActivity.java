package com.proprog.booklisting;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Book>>
        , SearchView.OnQueryTextListener {
    static String URL_QUERY = "https://www.googleapis.com/books/v1/volumes?q=android";
    ArrayList<Book> books;
    ListView listView;
    BookListAdapter bookListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0,null,this).forceLoad();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_book_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new BookTaskLoader(getApplicationContext(),URL_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        books = new ArrayList<>();
        books.addAll(data);
        listView = (ListView) findViewById(R.id.book_list_view);
        Log.d(this.getLocalClassName(),books.size()+"");
        bookListAdapter = new BookListAdapter(this,books);
        listView.setAdapter(bookListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        ArrayList<Book> filterBooks = new ArrayList<>();
//        newText = newText.toLowerCase();
//        for(Book  bookProvider : books){
//            String match = bookProvider.getTitle().toLowerCase();
//            if(match.contains(newText)){
//                filterBooks.add(bookProvider);
//            }
//        }
//        bookListAdapter.setFilter(filterBooks);
//        bookListAdapter.notifyDataSetChanged();
//        listView.setAdapter(bookListAdapter);
        return false;
    }
}
