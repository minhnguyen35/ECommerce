package com.example.ecommerce;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
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
        final ArrayList<String> listItem = new ArrayList<>();
        listItem.add("1235");
        listItem.add("1100");
        Button test = findViewById(R.id.main_test);
        loadingBar = new ProgressDialog(this);

        loadingBar.setCanceledOnTouchOutside(false);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.show();
                getOrderItem(listItem);
            }
        });
    }
    public void getOrderItem(final ArrayList<String> items)
    {

        final ArrayList<Item> listOrderItem = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Items");
                for(int i = 0; i <items.size(); i++) {
                    String id = items.get(i);

                    if (itemList.child(id).exists()) {
                        String ID = itemList.child(id).getValue().toString();
                        String CategoryID = itemList.child(id).child("categoryID").getValue().toString();

                        String Name= itemList.child(id).child("name").getValue().toString();
                        long Price = itemList.child(id).child("price").getValue(Long.class);

                        int Quantity = itemList.child(id).child("quantity").getValue(Long.class).intValue();
                        String Description= (String) itemList.child(id).child("description").getValue();
                        ArrayList<String> tmp = new ArrayList<>();
                        for(DataSnapshot x: itemList.child(id).child("imageArrayList").getChildren())
                            tmp.add(x.getValue().toString());
                        Item addItem= new Item(ID, CategoryID, Name, Price, null, Quantity, Description);

                        listOrderItem.add(addItem);

                    } else {
                        CharSequence s = "Item out of stock";
                        Toast toast = Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                //Toast.makeText(MainActivity.this, listOrderItem.get(1).getName(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                Toast.makeText(MainActivity.this, listOrderItem.get(1).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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