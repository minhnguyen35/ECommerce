package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewUserOrder extends AppCompatActivity {

    private ArrayList<User_Order> userOrderList;
    private String userID;

    private GridView gridView;
    private ViewUserOrder_Adapter adapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_order);


        initView();
        adaptView();
        clickOrderElement();
    }

    private void clickOrderElement() {
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent viewOrder = new Intent(ViewUserOrder.this, ViewUserOrderItem.class);
                viewOrder.putExtra("orderID", userOrderList.get(i).getId());
                startActivity(viewOrder);
            }
        });
    }

    private void adaptView() {
        adapter = new ViewUserOrder_Adapter(ViewUserOrder.this, R.layout.order_element, userOrderList);
        gridView.setAdapter(adapter);
    }

    private void initView() { gridView = findViewById(R.id.orderList); }

}