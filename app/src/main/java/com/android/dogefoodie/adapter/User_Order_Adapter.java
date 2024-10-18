package com.android.dogefoodie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.Order;
import com.android.dogefoodie.R;

import java.util.List;

public class User_Order_Adapter extends RecyclerView.Adapter<User_Order_Adapter.ViewHolder> {
    private List<Order> orderList;

    public User_Order_Adapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public User_Order_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_Order_Adapter.ViewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.txtStatus.setText(String.format("Order Status: %s", order.getOrderStatus()));
        holder.txtItem.setText(order.getProduct());
        holder.txtPrice.setText(String.format("$%.2f", order.getPrice()));
        holder.txtQnt.setText(String.format("x %s", order.getQuantity()));
        holder.txtTotal.setText(String.format("$%.2f", order.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatus, txtItem, txtPrice, txtQnt, txtTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtItem = itemView.findViewById(R.id.txtOrderItem);
            txtPrice = itemView.findViewById(R.id.txtOrderPrice);
            txtQnt = itemView.findViewById(R.id.txtOrderQnt);
            txtTotal = itemView.findViewById(R.id.txtOrderTotal);
        }
    }
}
