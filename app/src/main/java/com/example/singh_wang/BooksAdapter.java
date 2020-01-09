package com.example.singh_wang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Book> {
    Context _context;
    public BooksAdapter(Context context, ArrayList<Book> toons) {
        super(context, 0, toons);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Activity activity = (Activity) _context;
        // Get the data item for this position
        Book book = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_titles, parent, false);
        }
        // Lookup view for data population
        TextView tvFirstName = convertView.findViewById(R.id.title);
        // Populate the data into the template view using the data object
        tvFirstName.setText(book.getTitle());

        // Return the completed view to render on screen
        return convertView;
    }
}

