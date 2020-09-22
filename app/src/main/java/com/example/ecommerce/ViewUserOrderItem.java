package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewUserOrderItem extends AppCompatActivity {

    private ArrayList<Order_Item> userOrderItemList;
    private String orderID;
    private boolean status;
    private long orderTotal;

    private GridView gridView;
    private ViewUserOrderItem_Adapter adapter;
    private TextView oTotal;
    private Button BtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_order_item);

        getData();
        initBtn();
        adaptView();
        clickConfirm();
    }


    /*TODO:
    *  get list of order item from database by orderID*/


    private void clickConfirm() {
        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == false) {

                    /*TODO:
                    *  update status to TRUE in database*/

                    finish();
                }
            }
        });
    }

    private void adaptView() {
        oTotal.setText(Long.toString(orderTotal));
        adapter = new ViewUserOrderItem_Adapter(ViewUserOrderItem.this, R.layout.order_item_element, userOrderItemList);
        gridView.setAdapter(adapter);
    }

    private void initBtn() {
        gridView = findViewById(R.id.orderItemList);
        oTotal = findViewById(R.id.ototal);
        BtnConfirm = findViewById(R.id.btn_confirm_finish);
    }

    private void getData() {
        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");
        status = intent.getBooleanExtra("orderStatus", false);
        orderTotal = intent.getLongExtra("orderTotal", 0);
    }
}