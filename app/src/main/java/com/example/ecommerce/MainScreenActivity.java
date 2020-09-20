package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    public static ArrayList<Category> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        createRecyclerView();
    }

    private void createRecyclerView(){
        recyclerView = findViewById(R.id.listCategory);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

//        categoryArrayList = new ArrayList<>();
//        categoryArrayList.add(new Category(1,"Phone",ThisIsDraft()));
//        categoryArrayList.add(new Category(2,"Laptop",ThisIsDraft()));
//        categoryArrayList.add(new Category(3,"Plant",ThisIsDraft()));
//        categoryArrayList.add(new Category(4,"PC",ThisIsDraft()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        //adapter=new CategoryAdapter(this,categoryArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Item> ThisIsDraft(){

        ArrayList<Item> itemArrayList = new ArrayList<>();
//        ArrayList<Integer> imageList = new ArrayList<>();
        ArrayList<String> imageList = new ArrayList<>();
//        imageList.add(R.drawable.vietnam);
//        imageList.add(R.drawable.usa);
//        imageList.add(R.drawable.uk);
//        imageList.add(R.drawable.japan);
//        imageList.add(R.drawable.eur);

        itemArrayList.add(new Item("1","2","VN",10000,imageList,100,"vietnamese flag"));
        itemArrayList.add(new Item("2","2","USA Flag",10,imageList,51,"nothing"));
        itemArrayList.add(new Item("3","2","uk",10000,imageList,100,"uk flag ne"));
        itemArrayList.add(new Item("4","2","jp",10000,imageList,100,"japan fffff"));
        itemArrayList.add(new Item("5","2","eur",10000,imageList,100,"heheheheh"));

        return itemArrayList;
    }

    
}


