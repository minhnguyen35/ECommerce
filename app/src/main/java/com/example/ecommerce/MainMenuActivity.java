package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    ArrayList<Supermarket> supermarketArrayList;
    RecyclerView recyclerView;
    SupermarketAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    static public String acc;

    private final int RESULT_LOGOUT = 88888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        catchIntent();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(botNavBarListener);

        mapping();
        initSupermarketList();
        activateAdapter();

    }

    private void catchIntent() {
        Intent getAcc = getIntent();
        acc = getAcc.getStringExtra("account");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener botNavBarListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()){
                case R.id.userInfo:
                    intent = new Intent(MainMenuActivity.this, ViewUserInfo.class);
                    intent.putExtra("account",acc);
                    startActivity(intent);
                    return true;
                case R.id.order:
                    intent = new Intent(MainMenuActivity.this, ViewUserOrder.class);
                    intent.putExtra("account",acc);
                    startActivity(intent);
                    //Toast.makeText(MainMenuActivity.this,item.getTitle(),Toast.LENGTH_LONG).show();
                    return true;
                case R.id.logout:
                    logout();
                    return true;
            }
            return false;
        }
    };

    private void logout(){
        SharedPreferences sharedPref = getSharedPreferences("checkbox", Context.MODE_PRIVATE); // cai nay la de bo cai ghi nho dang nhap
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("remember", "false");
        editor.apply();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_LOGOUT)
            logout();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            clearSupermarketList();
            DataSnapshot itemList = snapshot.child("Supermarkets");
            for(DataSnapshot it: itemList.getChildren())
            {
                Supermarket supermarket = it.getValue(Supermarket.class);
                supermarketArrayList.add(supermarket);
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


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

    void mapping() {
        recyclerView = findViewById(R.id.recyclerViewSupermarket);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    void initSupermarketList(){
        supermarketArrayList = new ArrayList<>();
        clearSupermarketList();
    }

    void clearSupermarketList(){
        supermarketArrayList.clear();
        supermarketArrayList.add(new Supermarket("-1","All Supermarket",""));
    }


    void activateAdapter(){
        adapter = new SupermarketAdapter(MainMenuActivity.this, supermarketArrayList);
        recyclerView.setAdapter(adapter);
    }

}
