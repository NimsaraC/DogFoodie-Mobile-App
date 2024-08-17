package com.android.dogefoodie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.Order;
import com.android.dogefoodie.R;

import java.util.List;

public class User_History_Adapter extends RecyclerView.Adapter<User_History_Adapter.ViewHolder> {

    private List<Order> orderList;
    private Context context;

    public User_History_Adapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public User_History_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_History_Adapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtProduct.setText(order.getProduct());
        holder.txtPrice.setText(String.format("$%.2f", order.getPrice()));
        holder.txtQuantity.setText(String.valueOf(order.getQuantity()));
        holder.txtAddress.setText(order.getAddress());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProduct, txtPrice, txtQuantity, txtAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtAddress = itemView.findViewById(R.id.txtAddress);
        }
    }
}
