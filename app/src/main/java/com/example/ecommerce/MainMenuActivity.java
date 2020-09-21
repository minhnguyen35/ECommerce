package com.example.ecommerce;

import androidx.annotation.NonNull;
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
    String acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.appToolbar);
        Intent getAcc = getIntent();
        acc = getAcc.getStringExtra("account");
        setSupportActionBar(toolbar);

        mapping();
        initSupermarketList();
        activateAdapter();

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

                //Toast.makeText(MainMenuActivity.this, acc,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainMenuActivity.this, ViewUserInfo.class);
                intent.putExtra("account",acc);
                startActivity(intent);

                return true;
            case R.id.inCart:
                Toast.makeText(MainMenuActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            case R.id.order:
                Toast.makeText(MainMenuActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            case R.id.logout:
                SharedPreferences sharedPref = getSharedPreferences("checkbox", Context.MODE_PRIVATE); // cai nay la de bo cai ghi nho dang nhap
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("remember", "false");
                editor.apply();
                Intent intentLogout = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intentLogout);
                //Toast.makeText(MainMenuActivity.this,item.getTitle(),Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            clearSupermarketList();
            DataSnapshot itemList = snapshot.child("Supermarkets");//co s vao :)) tu`. de t lam cai nay
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

    void testingInitSupermarket() {
        /*Supermarket mega_market = new Supermarket("0",
                "Mega Market",
                "https://png2.cleanpng.com/sh/3a6d34f28d9d89d5c578008f3045e880/L0KzQYm3VsAyN6V5gZH0aYP2gLBuTf1uNZ5qf9M2bXH1e7b7TfFvNaFtRd92LX3od7K0jfFzc5Z5RdR3aD3zeH70Tf1uNZ10f9G2NXK0QYHpU8dma2Y8fag3M0C5SIa3VscyPWM8UKY8NkO8SYeBUb5xdpg=/kisspng-mm-mega-market-an-ph-mm-mega-market-bnh-ph-m-mm-logo-5b110b37ec57e6.3068506715278436399681.png"
        );
        Supermarket coopmart = new Supermarket("1",
                "Coop Mart",
                "https://tiepthigiadinh.vn/wp-content/uploads/2017/12/logo-coopmart-756x380.png");
        Supermarket lotte_mart = new Supermarket("2",
                "Lotte Mart",
                "https://img2.pngio.com/filelotte-mart-2018svg-wikimedia-commons-lotte-mart-png-1672_391.png");
        Supermarket vinmart = new Supermarket("3",
                "Vinmart",
                "https://static.ybox.vn/2018/10/1/1540182368009-Cover.png");

        ArrayList<Branch> mmBranches = new ArrayList<>();
        double[][] d = {{10.762913,106.679983},
                {10.8699184,106.8016194},
                {10.7859845,106.7011475},
                {10.8775848,106.7994309},
                {10.7830898,106.6925547},
                {10.772603,106.6555093},
                {10.8709321,106.7760666},
                {10.7906505,106.6806522},
                {10.8046919,106.714779}
        };
        Log.d("AAA",String.valueOf(d.length));
        Log.d("AAA",String.valueOf(d[0][0]));
        mmBranches.add(new Branch(mega_market,"0","Hà Nội", d[0],null));
        mmBranches.add(new Branch(mega_market, "1", "Thanh Hóa", d[1],null));

        ArrayList<Branch> coBranches = new ArrayList<>();
        coBranches.add(new Branch(coopmart, "0", "Hà Nội", d[2], null));
        coBranches.add(new Branch(coopmart, "1", "Thành Phố Hồ Chí Minh", d[3],null));
        coBranches.add(new Branch(coopmart, "2", "Thừa Thiên - Huế", d[4], null));

        ArrayList<Branch> loBranches = new ArrayList<>();
        loBranches.add(new Branch(lotte_mart, "0", "Đà Nẵng", d[5],null));
        loBranches.add(new Branch(lotte_mart, "1", "Bình Dương", d[6],null));

        ArrayList<Branch> vmBranches = new ArrayList<>();
        vmBranches.add(new Branch(vinmart, "0", "Nghệ An", d[7],null));
        vmBranches.add(new Branch(vinmart, "1", "Bắc Giang", d[8], null));

        supermarketArrayList = new ArrayList<>();
        supermarketArrayList.add(new Supermarket("-1","All Markets",""));
        supermarketArrayList.add(mega_market);
        supermarketArrayList.add(coopmart);
        supermarketArrayList.add(lotte_mart);
        supermarketArrayList.add(vinmart);

        ArrayList<Branch> all = new ArrayList<>();
        all.addAll(mmBranches);
        all.addAll(coBranches);
        all.addAll(loBranches);
        all.addAll(vmBranches);

        branches = new ArrayList<>();
        branches.add(all);
        branches.add(mmBranches);
        branches.add(coBranches);
        branches.add(loBranches);
        branches.add(vmBranches);
*/
    }

}
