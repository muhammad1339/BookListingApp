package com.proprog.booklisting;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohamedAHMED on 2017-11-10.
 */

public class BookListAdapter extends ArrayAdapter<Book> {
    private Context context;
//    private ArrayList<Book> bookProviders;
    public BookListAdapter(@NonNull Context context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
//        this.bookProviders = books;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View bookItemView = convertView;
        ViewHolder holder;
        if (bookItemView == null) {
            bookItemView = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
            holder = new ViewHolder(bookItemView);
            bookItemView.setTag(holder);
        } else {
            holder = (ViewHolder) bookItemView.getTag();
        }
        Book book = getItem(position);
        if (book != null) {
            holder.bookTitle.setText(book.getTitle());
            holder.bookPublisher.setText(book.getPublisher());
            Picasso.with(context).load(book.getThumbnail()).into(holder.bookImage);
        }
        return bookItemView;
    }

    static class ViewHolder {
        @BindView(R.id.book_image)
        ImageView bookImage;
        @BindView(R.id.book_title)
        TextView bookTitle;
        @BindView(R.id.book_publisher)
        TextView bookPublisher;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
//    public void setFilter(ArrayList<Book> books){
//        bookProviders = new ArrayList<>();
//        bookProviders.addAll(books);
//        notifyDataSetChanged();
//    }
}
