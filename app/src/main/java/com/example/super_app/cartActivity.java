package com.example.super_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.super_app.db.CartProductAdapter;
import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.OrderAdapter;
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

public class cartActivity extends AppCompatActivity implements DeleteOrderInterface {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Product> products = new ArrayList<>();
    private CartProductAdapter cardProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Context context = getApplicationContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Order order = doc.toObject(Order.class);
                        if (order.getProductsOfNeigh() != null) {
                            HashMap<String, ArrayList<Product>> productsPerUser = order.getProductsOfNeigh();
                            if (productsPerUser.containsKey(mAuth.getUid())) {
                                for (Product product : productsPerUser.get(mAuth.getUid())) {
                                    products.add(product);
                                    //add product to sql lite
                                    dbHelper.insertProduct(product, order.getId());
                                }
                            }
                        }
                    }
                    cardProductAdapter = new CartProductAdapter(products, cartActivity.this, cartActivity.this);
                    recyclerView.setAdapter(cardProductAdapter);
                }

            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return false;
            }
        });


    }

    private void fileList(String newText) {
        List<Product> filteredList= new ArrayList<>();
        for(Product product:products)
        {
            if (product.getName().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(product);
            }
        }
        if(filteredList.isEmpty())
        {
            Toast.makeText(this,"No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            cardProductAdapter.setFilteredList(filteredList);
        }

    }

    @Override
    public void onDeleteClick(int position) {
        showSimpleAlertDialogDeleteProduct(position);
    }

    private void showSimpleAlertDialogDeleteProduct(int position) {
        Product product = products.get(position);
        AlertDialog.Builder builder = null;
        Context context = getApplicationContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        builder = new AlertDialog.Builder(cartActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.getoutProduct);
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Order order = doc.toObject(Order.class);
                                if (order.getProductsOfNeigh() != null) {
                                    HashMap<String, ArrayList<Product>> productsPerUser = order.getProductsOfNeigh();
                                    if (productsPerUser.containsKey(mAuth.getUid())) {
                                        ArrayList<Product> list = productsPerUser.get(mAuth.getUid());
                                        boolean b = list.remove(product);
                                        if (b) {
                                            productsPerUser.put(mAuth.getUid(), list);
                                            order.setProductsOfNeigh(productsPerUser);
                                            db.collection("Orders").document(doc.getId()).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        if (cardProductAdapter != null) {
                                                            products.remove(product);
                                                            dbHelper.deleteProduct(product);
                                                            cardProductAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }
                                            });
                                        }

                                    }
                                }
                            }
                        }
                    }
                });

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Toast.makeText(getApplicationContext(), "Users cancelled the dialog", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onJoinClick(int position) {

    }
}
