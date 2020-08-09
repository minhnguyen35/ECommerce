package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent (MainActivity.this, MainScreenActivity.class);
        startActivity(intent);

        //setUpLander();
        //setContentView(R.layout.list_item_display);
        //initItemView();


    }

    private void setUpLander() {
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

    public void initItemView(){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerViewListItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ArrayList<Item> itemArrayList = new ArrayList<>();
//        itemArrayList.add(new Item(R.drawable.vietnam,"VietNam Flag",6.3));
//        itemArrayList.add(new Item(R.drawable.usa,"USA Flag",5.1));
//        itemArrayList.add(new Item(R.drawable.eur,"Europe Flag",2.7));
//        itemArrayList.add(new Item(R.drawable.uk,"UK Flag",4));
//        itemArrayList.add(new Item(R.drawable.japan,"Japan Flag",3));

        ItemAdapter itemAdapter = new ItemAdapter(this, itemArrayList);
        recyclerView.setAdapter(itemAdapter);
    }


}