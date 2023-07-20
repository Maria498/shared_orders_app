package com.example.super_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectionActivity extends Activity {

    private static final int REQUEST_IMAGE_SELECT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);

        // Get a list of all drawable resource IDs
        List<Integer> drawableResourceIds = new ArrayList<>();
        drawableResourceIds.add(R.drawable.apple);
        drawableResourceIds.add(R.drawable.strawberry);
        drawableResourceIds.add(R.drawable.lime);
        // Add more drawable resource IDs as needed

        ListView listView = findViewById(R.id.listView);

        // Use the custom ImageAdapter to display images and text in the list
        ImageAdapter adapter = new ImageAdapter(this, drawableResourceIds);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            int selectedImageResourceId = drawableResourceIds.get(position);

            // Pass the selected image resource ID back to the AdminActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("imageResourceId", selectedImageResourceId);
            setResult(RESULT_OK, resultIntent);

            finish();
        });
    }
}