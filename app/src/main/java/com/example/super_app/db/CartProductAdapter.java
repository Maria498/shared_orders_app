package com.example.super_app.db;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.DeleteOrderInterface;
import com.example.super_app.R;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private DeleteOrderInterface deleteOrderInterface;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public void setFilteredList(List<Product> filteredList){
        this.productList=filteredList;
        notifyDataSetChanged();
    }


    public CartProductAdapter(List<Product> productList, Context context, DeleteOrderInterface deleteOrderInterface) {
        this.productList = productList;
        this.context = context;
        this.deleteOrderInterface = deleteOrderInterface;

    }

    public void setItems(List<Product> list) {
        this.productList = list;
    }

    public List<Product> getOrderList() {
        return productList;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public CartProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CartProductAdapter.ProductViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.setData(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView vProductName;
        private TextView vProductCategory;
        private TextView vProductQuantity;
        private TextView vProductPrice;
        private ImageButton vBtnRemove;
        private ImageButton vMinusButton;
        private ImageButton vPlusButton;
        private TextView vDigit;
        private Product product = null;
        private Product newProduct=null;

        public ProductViewHolder(View v) {
            super(v);
            vProductName = v.findViewById(R.id.productName);
            vProductCategory = v.findViewById(R.id.productCategory);
            vProductQuantity = v.findViewById(R.id.productQuantity);
            vProductPrice = v.findViewById(R.id.productPrice);
            vBtnRemove = v.findViewById(R.id.btnRemove);
            vMinusButton = v.findViewById(R.id.minusButton);
            vPlusButton = v.findViewById(R.id.pluseButton);
            vDigit = v.findViewById(R.id.digitEditText);


            vMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double newPrice;
                    if (newProduct != null) {
                        int quantity = newProduct.getQuantity();
                        if (quantity > 1) {
                            quantity--;
                            vProductQuantity.setText(String.valueOf(quantity));
                            if (newProduct.getDiscount() != 0) {
                                newPrice = newProduct.getPrice() * (100 - newProduct.getDiscount()) / 100;
                            } else {
                                newPrice = newProduct.getPrice();
                            }
                            vProductPrice.setText("" + quantity * newPrice);
                            vDigit.setText("" + quantity);
                            newProduct.setQuantity(quantity);
                            newProduct.setPrice(newPrice);
                            db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            Order order = doc.toObject(Order.class);
                                            if (order.getProductsOfNeigh() != null) {
                                                HashMap<String, ArrayList<Product>> productsPerUser = order.getProductsOfNeigh();
                                                if (productsPerUser.containsKey(mAuth.getUid())) {
                                                    ArrayList<Product> list=productsPerUser.get(mAuth.getUid());
                                                    list.remove(product);
                                                    list.add(newProduct);
                                                    productsPerUser.put(mAuth.getUid(),list);
                                                    order.setProductsOfNeigh(productsPerUser);
                                                    db.collection("Orders").document(doc.getId()).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                Toast.makeText(getContext(), "saved changes", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                                }

                                            } else {
                                                Log.e("Firebase", "Error getting order document: ", task.getException());
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
            vPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double newPrice;
                    if (newProduct != null) {
                        int quantity = newProduct.getQuantity();
                        if (quantity < 10) {
                            quantity++;
                            vProductQuantity.setText(String.valueOf(quantity));
                            if (newProduct.getDiscount() != 0) {
                                newPrice = newProduct.getPrice() * (100 - newProduct.getDiscount()) / 100;
                            } else {
                                newPrice = newProduct.getPrice();
                            }
                            vProductPrice.setText("" + quantity * newPrice);
                            vDigit.setText("" + quantity);
                            newProduct.setQuantity(quantity);
                            newProduct.setPrice(newPrice);
                            db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            Order order = doc.toObject(Order.class);
                                            if (order.getProductsOfNeigh() != null) {
                                                HashMap<String, ArrayList<Product>> productsPerUser = order.getProductsOfNeigh();
                                                if (productsPerUser.containsKey(mAuth.getUid())) {
                                                    ArrayList<Product> list=productsPerUser.get(mAuth.getUid());
                                                    list.remove(product);
                                                    list.add(newProduct);
                                                    productsPerUser.put(mAuth.getUid(),list);
                                                    order.setProductsOfNeigh(productsPerUser);
                                                    db.collection("Orders").document(doc.getId()).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                Toast.makeText(getContext(), "saved changes", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                                }

                                            } else {
                                                Log.e("Firebase", "Error getting order document: ", task.getException());
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

            vBtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteOrderInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteOrderInterface.onDeleteClick(position);
                        }
                    }
                }
            });

        }

        public void setData(Product product) {
            this.product = product;
            this.newProduct=product;
            vProductName.setText(product.getName());
            vProductCategory.setText(product.getCategory());
            vProductQuantity.setText(String.valueOf(product.getQuantity()));
            vProductPrice.setText(String.valueOf(product.getPrice()));
            vDigit.setText(String.valueOf(product.getQuantity()));
        }
    }


}
