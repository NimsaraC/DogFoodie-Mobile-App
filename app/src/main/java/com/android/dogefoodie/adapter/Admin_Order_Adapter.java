package com.android.dogefoodie.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.Order;
import com.android.dogefoodie.R;
import com.android.dogefoodie.database.OrderDB;

import java.util.List;

public class Admin_Order_Adapter extends RecyclerView.Adapter<Admin_Order_Adapter.ViewHolder> {
    private List<Order> orderList;
    Context context;

    public Admin_Order_Adapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public Admin_Order_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Order_Adapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtId.setText(String.format("Order Id: %s", order.getId()));
        holder.txtProduct.setText(String.format("Product: %s", order.getProduct()));
        holder.txtQuantity.setText(String.format("Quantity: %s", order.getQuantity()));
        holder.txtTotal.setText(String.format("Order Id: %s", order.getTotalPrice()));
        holder.txtAddress.setText(String.format("Address: %s", order.getAddress()));
        holder.txtStatus.setText(order.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtProduct, txtQuantity, txtAddress, txtTotal, txtStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtOrderId);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtQuantity = itemView.findViewById(R.id.txtQnt);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
