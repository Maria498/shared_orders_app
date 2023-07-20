package com.example.super_app;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<Integer> {

    private final List<Integer> drawableResourceIds;
    private final LayoutInflater inflater;

    public ImageAdapter(Context context, List<Integer> drawableResourceIds) {
        super(context, 0, drawableResourceIds);
        this.drawableResourceIds = drawableResourceIds;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_image, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);

        int resourceId = drawableResourceIds.get(position);
        Drawable drawable = getContext().getResources().getDrawable(resourceId);
        imageView.setImageDrawable(drawable);

        textView.setText("Image " + (position + 1)); // You can customize the text here if needed

        return view;
    }
}
