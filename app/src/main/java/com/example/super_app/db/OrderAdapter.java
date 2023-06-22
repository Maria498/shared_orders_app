package com.example.super_app.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.R;
import com.example.super_app.db.entity.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
   private static List<Order> orderList;
   private static Context context;

    public OrderAdapter(List<Order> orderList,Context context) {
        this.orderList=orderList;
        this.context=context;
    }


    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vieworderinfo,parent,false);
        return new OrderViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order order=orderList.get(position);
        holder.setData(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView vName;
        private TextView vPhone;
        private TextView vAddress;
        private TextView vDate;
        private ImageButton vBtn;
        private Order order=null;
        public OrderViewHolder(View v)
        {
            super(v);
            vName = v.findViewById(R.id.txtCreatorName);
            vPhone = v.findViewById(R.id.txtPhone);
            vAddress = v.findViewById(R.id.txtAddress);
            vDate = v.findViewById(R.id.txtDeliveryDate);
            vBtn = v.findViewById(R.id.Btn);
            vBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void setData(Order order) {
            this.order=order;
            vName.setText(order.getFullNameOwner());
            vPhone.setText(order.getPhoneNumberOwner());
            vAddress.setText(order.getAddress());
            vDate.setText(order.getDeliveryDate());
        }
    }
}
