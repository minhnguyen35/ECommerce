package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private CategoryAdapter adapter;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Integer> categoryIDList;
    private String branchID;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        catchIntent();
        createRecyclerView();
    }

    void catchIntent(){
        Intent intent = getIntent();
        categoryIDList = intent.getIntegerArrayListExtra("category");
        branchID = intent.getStringExtra("branchID");
    }

    private void createRecyclerView(){
        recyclerView = findViewById(R.id.listCategory);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        /*categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category(1,"Phone",ThisIsDraft()));
        categoryArrayList.add(new Category(2,"Laptop",ThisIsDraft()));
        categoryArrayList.add(new Category(3,"Plant",ThisIsDraft()));
        categoryArrayList.add(new Category(4,"PC",ThisIsDraft()));
*/
        categoryArrayList = new ArrayList<>();
        adapter=new CategoryAdapter(this,categoryArrayList);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    private ArrayList<Item> ThisIsDraft(){

        ArrayList<Item> itemArrayList = new ArrayList<>();
        ArrayList<String> imageList = new ArrayList<>();
/*
        imageList.add((Bitmap)BitmapFactory.decodeResource(getResources(),R.drawable.vietnam));
        imageList.add((Bitmap)BitmapFactory.decodeResource(getResources(),R.drawable.usa));
        imageList.add((Bitmap)BitmapFactory.decodeResource(getResources(),R.drawable.uk));
        imageList.add((Bitmap)BitmapFactory.decodeResource(getResources(),R.drawable.japan));
        imageList.add((Bitmap)BitmapFactory.decodeResource(getResources(),R.drawable.eur));

        itemArrayList.add(new Item("1",2,"VN",10000,imageList,100,"vietnamese flag"));
        itemArrayList.add(new Item("2",2,"USA Flag",5.1,imageList,51,"nothing"));
        itemArrayList.add(new Item("3",2,"uk",10000,imageList,100,"uk flag ne"));
        itemArrayList.add(new Item("4",2,"jp",10000,imageList,100,"japan fffff"));
        itemArrayList.add(new Item("5",2,"eur",10000,imageList,100,"heheheheh"));
*/
        return itemArrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.addListenerForSingleValueEvent(newEvent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(newEvent);
    }

    private ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            categoryArrayList = new ArrayList<>();
            DataSnapshot categoryList = snapshot.child("Category");
            for(DataSnapshot cate: categoryList.getChildren())
            {
                String brID = cate.child("branchID").getValue().toString();
                if(branchID.equals(brID)) {
                    Category newCate;
                    String categoryID = cate.child("id").getValue().toString();
                    String name = cate.child("name").getValue().toString();

                    ArrayList<Item>  itemArrayList = new ArrayList<>();
                    DataSnapshot itemList = snapshot.child("Items");
                    for(DataSnapshot it: itemList.getChildren())
                    {
                        String catID = it.child("categoryID").getValue().toString();
                        if(categoryID.equals(catID)) {
                            String ID = it.child("id").getValue().toString();
                            String Name= it.child("name").getValue().toString();
                            long Price = it.child("price").getValue(Long.class);
                            ArrayList<String> imageUrl = new ArrayList<>();
                            for(DataSnapshot url : it.child("imageArrayList").getChildren())
                            {
                                String curUrl = url.getValue().toString();
                                imageUrl.add(curUrl);
                            }
                            int Quantity = it.child("quantity").getValue(Long.class).intValue();
                            String Description= (String) it.child("description").getValue();
                            Item newItem = new Item(ID, catID, Name, Price, imageUrl, Quantity, Description);
                            itemArrayList.add(newItem);
                        }
                    }

                    newCate = new Category(categoryID, name,itemArrayList);
                    categoryArrayList.add(newCate);
                }
            }

            adapter.setCategoryArrayList(categoryArrayList);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}


