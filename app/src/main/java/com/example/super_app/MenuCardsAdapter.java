package com.example.super_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MenuCardsAdapter extends RecyclerView.Adapter<MenuCardsAdapter.VewHolder> {


    private Context context;
    private ArrayList<MenuModel> cardsList;

    public MenuCardsAdapter(Context context, ArrayList<MenuModel> cardsList) {
        this.context = context;
        this.cardsList = cardsList;
    }

    @NonNull
    @Override
    public VewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new VewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VewHolder holder, int position) {
        MenuModel model = cardsList.get(position);
        holder.cardTitle.setText(model.getCardTitle());
        holder.cardText.setText(model.getCardText());
        holder.cardImage.setImageResource(model.getCardImage());
        holder.itemView.setOnClickListener(v -> moveToActivity(model));

    }

    //  switching activities according to picked category
    private void moveToActivity (MenuModel model) {
        Intent i = new Intent(context, MainActivity.class);;
        if (model.getCardTitle() == R.string.fruits) {
            i = new Intent(context, ProductsActivity.class);
            i.putExtra("msg", "Fruits");
        }
        else if (model.getCardTitle() == R.string.veggie) {
            i = new Intent(context, ProductsActivity.class);
            i.putExtra("msg", "Veggies");
        }
        else if (model.getCardTitle() == R.string.meat) {
            i = new Intent(context, ProductsActivity.class);
            i.putExtra("msg", "Meat");
        }
        context.startActivity(i);
    }


    @Override
    public int getItemCount() {
        return cardsList.size();
    }


    public class VewHolder extends RecyclerView.ViewHolder{

        private ImageView cardImage;
        private TextView cardTitle;
        private TextView cardText;

        public VewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardText = itemView.findViewById(R.id.cardText);
        }
    }


}
