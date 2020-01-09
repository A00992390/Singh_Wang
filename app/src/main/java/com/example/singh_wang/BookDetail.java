package com.example.singh_wang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // To retrieve object in second Activity
        Book book = (Book) getIntent().getSerializableExtra("book");

        // image
        ImageView photo = findViewById(R.id.bookphoto);
        //DownloadImageTask dit = new DownloadImageTask(_context, imgOnePhoto);
        //dit.execute(toon.getPicture());
        if (book.getThumbnail() != null) {
            new ImageDownloaderTask(photo).execute(book.getThumbnail());
        }
        // title name
        TextView title = findViewById(R.id.title);
        title.setText(book.getTitle());
        // authors
        TextView author = findViewById(R.id.author);
        String listAuthors = "";
        List<String> bookAuthors = book.getAuthors();
        for (String s : book.getAuthors()){
            if (s != bookAuthors.get(bookAuthors.size() - 1)) {
                listAuthors += s + ",";
            } else {
                listAuthors += s;
            }
        }
        author.setText("Authors: " + listAuthors);
        //description
        TextView des = findViewById(R.id.des);
        des.setText(book.getDescription());
        des.setMovementMethod(new ScrollingMovementMethod());
        // publisher
        TextView publisher = findViewById(R.id.publisher);
        publisher.setText("Publisher: " + book.getPublisher());
        // date
        TextView date = findViewById(R.id.publishdate);
        date.setText("Published Date: " + book.getPublishedDate());
        // isbn-10
        TextView isbn = findViewById(R.id.isbn);
        isbn.setText("ISBN: " + book.getISBN_10());
    }
}
