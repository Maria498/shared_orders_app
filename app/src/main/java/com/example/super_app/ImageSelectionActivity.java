package com.example.super_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectionActivity extends Activity {

    private static final int REQUEST_IMAGE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);
        ListView listView = findViewById(R.id.listView);
        // Get a list of all drawable resource IDs
        List<Integer> drawableResourceIds = new ArrayList<>();
        drawableResourceIds.add(R.drawable.apple);
        drawableResourceIds.add(R.drawable.strawberry);
        drawableResourceIds.add(R.drawable.lime);
        drawableResourceIds.add(R.drawable.orange);
        drawableResourceIds.add(R.drawable.broccoli);
        drawableResourceIds.add(R.drawable.cabbage);
        drawableResourceIds.add(R.drawable.pepper);
        drawableResourceIds.add(R.drawable.chicken);
        drawableResourceIds.add(R.drawable.steak);
        drawableResourceIds.add(R.drawable.coke);
        drawableResourceIds.add(R.drawable.pepper);
        drawableResourceIds.add(R.drawable.sprite);
        drawableResourceIds.add(R.drawable.croissant);
        drawableResourceIds.add(R.drawable.reeses);
        drawableResourceIds.add(R.drawable.fridge);

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