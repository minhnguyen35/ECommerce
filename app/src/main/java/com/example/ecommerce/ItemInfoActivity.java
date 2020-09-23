package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemInfoActivity extends AppCompatActivity {
    Item item;
    ArrayList<Item> suggestionItemList;
    RecyclerView recyclerViewArrayImage, recyclerViewArraySuggestion;
    TextView textViewName, textViewPrice, textViewQuantity,textViewID,textViewCateID, textViewDescr;
    Button btnAdd;

    ItemAdapter suggestionAdapter;
    ImageAdapter imageAdapter;

    private String categoryID;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Toolbar toolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);

        mapping();
        catchIntent();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.userInfo:
                //todo: please intent here

                /*
                Intent intent = new Intent(MainMenuActivity.this, HomeActivity.class);
                startActivity(intent);
                */
                Toast.makeText(ItemInfoActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            case R.id.inCart:
                Toast.makeText(ItemInfoActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            case R.id.order:
                Toast.makeText(ItemInfoActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            case R.id.logout:
                Toast.makeText(ItemInfoActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.addValueEventListener(newEvent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(newEvent);
    }

    ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            suggestionItemList = new ArrayList<>();
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
                    if(!ID.equals(item.getID()))
                        suggestionItemList.add(newItem);
                    else{
                        item = newItem;
                        imageAdapter.setImageArray(item.getImageArrayList());
                        updateItemInfo();
                    }
                }
            }

            suggestionAdapter.setItemArrayList(suggestionItemList);
            suggestionAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void catchIntent() {
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        categoryID = item.getCategoryID();


    }

    private void mapping(){
        recyclerViewArrayImage=findViewById(R.id.recyclerViewArrayImage);
        recyclerViewArraySuggestion=findViewById(R.id.recyclerViewArraySuggestion);
        textViewName=findViewById(R.id.textViewItemName);
        textViewID=findViewById(R.id.textViewItemID);
        textViewCateID=findViewById(R.id.textViewCategoryID);
        textViewPrice=findViewById(R.id.textViewItemPrice);
        textViewQuantity=findViewById(R.id.textViewItemQuantity);
        textViewDescr=findViewById(R.id.textViewItemDescription);
        btnAdd=findViewById(R.id.buttonAddToCart);
    }

    private void init(){
        recyclerViewArrayImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewArrayImage.setHasFixedSize(true);
        imageAdapter = new ImageAdapter(this,item.getImageArrayList());
        recyclerViewArrayImage.setAdapter(imageAdapter);
        //imageAdapter.notifyDataSetChanged();

        recyclerViewArraySuggestion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewArraySuggestion.setHasFixedSize(true);
        suggestionItemList = new ArrayList<>();
        suggestionAdapter = new ItemAdapter(this, suggestionItemList, false);
        recyclerViewArraySuggestion.setAdapter(suggestionAdapter);
        //suggestionAdapter.notifyDataSetChanged();

        updateItemInfo();
        btnAdd.setOnClickListener(add);
    }

    private void updateItemInfo(){
        textViewName.setText(item.getName());
        textViewID.setText(item.getID());
        textViewCateID.setText(String.valueOf(item.getCategoryID()));
        textViewPrice.setText(String.valueOf(item.getPrice()));
        textViewQuantity.setText(String.valueOf(item.getQuantity()));
        textViewDescr.setText(item.getDescription());
        imageAdapter.notifyDataSetChanged();
    }
    Button.OnClickListener add = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(ItemInfoActivity.this,"Add to Cart",Toast.LENGTH_LONG).show();
        }
    };




}