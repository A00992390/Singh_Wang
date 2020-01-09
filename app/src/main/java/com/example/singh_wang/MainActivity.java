package com.example.singh_wang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    // URL to get contacts JSON
    private static String SERVICE_URL = "https://www.googleapis.com/books/v1/volumes?q=harry+potter";
    private ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = new ArrayList<Book>();
        lv = findViewById(R.id.bookList);
        new GetContacts().execute();

        // Listener for each list item
        ListView list_continents = findViewById(R.id.bookList);
        list_continents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, BookDetail.class);
                // sending required information to BookDetailActivity
                intent.putExtra("book", bookList.get(i));
                startActivity(intent);
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = null;

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(SERVICE_URL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray items = jsonObj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        JSONObject volInfo = item.getJSONObject("volumeInfo");
                        // image urls
                        JSONObject image = volInfo.getJSONObject("imageLinks");
                        // identifiers
                        JSONArray isbnArray = volInfo.getJSONArray("industryIdentifiers");

                        JSONObject isbn = isbnArray.getJSONObject(1);
                        // authors
                        JSONArray authors = volInfo.getJSONArray("authors");

                        String title = volInfo.getString("title");

                        String publisher = volInfo.getString("publisher");

                        String description = "";
                        if (volInfo.has("description")) {
                            description = volInfo.getString("description");
                        }

                        String publishedDate = volInfo.getString("publishedDate");

                        String ISBN_10 = isbn.getString("identifier");

                        List<String> authorsList = new ArrayList<>();

                        for(int x = 0; x < authors.length(); x++) {
                            authorsList.add(authors.getString(x));
                        }

                        String thumbnail = image.getString("thumbnail");

                        // tmp hash map for single contact
                        Book book = new Book();

                        // adding each child node to HashMap key => value
                        book.setTitle(title);
                        book.setAuthors(authorsList);
                        book.setDescription(description);
                        book.setPublishedDate(publishedDate);
                        book.setPublisher(publisher);
                        book.setISBN_10(ISBN_10);
                        book.setThumbnail(thumbnail);

                        // adding contact to contact list
                        bookList.add(book);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            BooksAdapter adapter = new BooksAdapter(MainActivity.this, bookList);

            // Attach the adapter to a ListView
            lv.setAdapter(adapter);
        }
    }
}
