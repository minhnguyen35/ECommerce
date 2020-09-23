package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initItemView();


        //Intent intent = new Intent(this, MainMenuActivity.class);
        //startActivity(intent);

        setUpLander();
        checkRemember();
        TestButton();
    }
    ProgressDialog loadingBar;
    private void TestButton()
    {
        Button test = findViewById(R.id.main_test);
        final int quantity = 100000;
        final long[] quant = new long[1];
        ArrayList<Order_Item> listOrder = new ArrayList<>();
        final ArrayList<Order_Item> orderItems = new ArrayList<>();
        Order_Item y = new Order_Item(null, null, "1235", null,
                1, 1, 20000000, 20000000);
        orderItems.add(y);
        final ArrayList<Integer>  listQuantityInDB = new ArrayList<>();
        quant[0] = 0;
        loadingBar = new ProgressDialog(this);
        loadingBar.setCanceledOnTouchOutside(false);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.show();
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                final Order_Item x = new Order_Item(null, null, "1243", null,
                1, 1, 23000, 23000);
                DatabaseReference itemObject = db.child("Items");
                itemObject.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        for(int i = 0; i < orderItems.size(); i++)
                        {
                            Order_Item newItem = orderItems.get(i);
                            int quantityOrd = newItem.getQuantity();
                            long quantityRealtmp = 0;// = currentData.child(newItem.getId()).child("quantity").getValue();
                            while (currentData.child(newItem.getId()).child("quantity").getValue() != null)
                            {
                                quantityRealtmp =(long) currentData.child(newItem.getId()).child("quantity").getValue();
                            }
                            int quantityReal = (int) quantityRealtmp;
                            listQuantityInDB.add(quantityReal);
                            quant[0] = quantityReal;
                            if(quantityOrd > quantityReal)
                            {

                                return Transaction.abort();
                            }
                            long priceOrd = newItem.getPrice();
                            long priceReal = (long) currentData.child(newItem.getId()).child("price").getValue();
                            if(priceOrd != priceReal)
                            {
                                return Transaction.abort();
                            }
                        }
                        for(int i = 0; i < orderItems.size();i++)
                        {
                            String id = orderItems.get(i).getId();
                            int quantityOrd = orderItems.get(i).getQuantity();
                            int quantityReal = listQuantityInDB.get(i);
                            currentData.child(id).child("quantity").setValue(quantityReal - quantityOrd);
                        }
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        if(committed)
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, String.valueOf(quant[0]), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }

    private void initItemView() {
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.register_login);
    }


    private void checkRemember()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String isCheck = sharedPreferences.getString("remember", "");
        String userAcc = sharedPreferences.getString("acc", "");
        if(isCheck.equals("true"))
        {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            intent.putExtra("account",userAcc);
            startActivity(intent);
        }
    }
    private void setUpLander() {
        TextView time = findViewById(R.id.time);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(System.currentTimeMillis());
        String date = DateFormat.format("HH:mm dd-MM-yyyy", cal).toString();
        time.setText(date);
        login = (Button)findViewById(R.id.main_login);
        register = (Button)findViewById(R.id.main_register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }




}