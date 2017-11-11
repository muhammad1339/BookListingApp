package com.proprog.booklisting;

import android.text.TextUtils;
import android.util.Log;

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
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamedAHMED on 2017-11-10.
 */

public class QueryUtils {

    private final String LOG_MSG_CLASS = QueryUtils.class.getSimpleName();

    public ArrayList<Book> fetchBookData(String queryUrl) {
        //prepare url
        URL url = createURL(queryUrl);
        //fetch json data from url
        String jsonResponse = makeHttpRequest(url);
        ArrayList<Book> books = null;
        try {
            books = extractBookFeature(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<Book> extractBookFeature(String jsonResponse) throws JSONException {
        Book book ;
        ArrayList<Book> books = new ArrayList<>();
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray items = root.getJSONArray("items");
        Log.d(LOG_MSG_CLASS, "extractBookFeature : "+String.valueOf(items.length()));

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject volumeInfo = item.getJSONObject("volumeInfo");
            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            String title = volumeInfo.getString("title");
            String publisher = volumeInfo.getString("publisher");
            String thumbnail = imageLinks.getString("thumbnail");
            if (publisher.length()==0) {
                break;
            } else {
                book = new Book(title, publisher, thumbnail);
//                Log.d(LOG_MSG_CLASS, i + " - " + title + "\n    " + publisher + "\n    " + thumbnail);
                books.add(book);
            }

        }
        return books;
    }

    public URL createURL(String urlQuery) {

        URL url = null;
        try {
            url = new URL(urlQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public String makeHttpRequest(URL url) {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                jsonResponse = readFromInputStream(urlConnection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //release Resources
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return jsonResponse;
    }

    public String readFromInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferReader = new BufferedReader(inputStreamReader);
        try {
            String line = bufferReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


}
