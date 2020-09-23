package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.ecommerce.MainMenuActivity.acc;

public class BranchMenuActivity extends AppCompatActivity {
    private String supermarketID;
    private ArrayList<Branch> branchArrayList;
    private ArrayList<Branch> selectedBranchArrayList;

    private ImageButton mapButton;
    private Spinner spinner;
    private RecyclerView recyclerView;

    private BranchAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private final int PERMISSION_REQUEST_CODE = 12345;
    private int REQUEST_CODE_CART = 10000, REQUEST_CODE_ACCOUNT = 20000, REQUEST_CODE_ORDERS = 30000;
    private int REQUEST_CODE_CATEGORY = 456;
    private int RESULT_LOGOUT = 88888;

    public static Bundle cart;
    private String avatar="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_menu);

        catchIntent();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(botNavBarListener);

        mapping();
        createRecyclerView();
        createListener();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener botNavBarListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.userInfo:
                            intent = new Intent(BranchMenuActivity.this, ViewUserInfo.class);
                            intent.putExtra("account",acc);
                            startActivity(intent);
                            return true;
                        case R.id.inCart:
                            intent = new Intent(BranchMenuActivity.this,PaymentActivity.class);
                            intent.putExtra("bundle",cart);
                            startActivity(intent);
                            return true;
                        case R.id.order:
                            Toast.makeText(BranchMenuActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.logout:
                            logout();
                            return true;
                    }
                    return false;
                }
            };

    private void logout() {
        SharedPreferences sharedPref = getSharedPreferences("checkbox", Context.MODE_PRIVATE); // cai nay la de bo cai ghi nho dang nhap
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("remember", "false");
        editor.apply();
        setResult(RESULT_LOGOUT);
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
        finish();
    }

    void mapping() {
        spinner = findViewById(R.id.spinnerCity);
        recyclerView = findViewById(R.id.recyclerViewBranches);
        mapButton = findViewById(R.id.buttonMap);
    }

    void catchIntent() {
        Intent intent = getIntent();
        supermarketID = intent.getStringExtra("supermarketID");
    }

    void createRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        selectedBranchArrayList = new ArrayList<>();

        adapter = new BranchAdapter(this, selectedBranchArrayList, false);
        recyclerView.setAdapter(adapter);
    }



    void createListener() {
        spinner.setOnItemSelectedListener(spinnerHelper);
        mapButton.setOnClickListener(mapHelper);
    }

    private boolean checkPermisson() {
        if (ContextCompat.checkSelfPermission(
                BranchMenuActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        BranchMenuActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BranchMenuActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    ImageButton.OnClickListener mapHelper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkPermisson()) {
                Intent intent = new Intent(BranchMenuActivity.this, MapsActivity.class);
                intent.putExtra("branch", branchArrayList);
                startActivity(intent);
            }
        }
    };


    Spinner.OnItemSelectedListener spinnerHelper = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (branchArrayList!=null) {
                selectedBranchArrayList = new ArrayList<>();
                String city = spinner.getSelectedItem().toString().toLowerCase();
                for (int b = 0; b < branchArrayList.size(); ++b) {
                    if (city.equals("all") || branchArrayList.get(b).getAddress().toLowerCase().contains(city))
                        selectedBranchArrayList.add(branchArrayList.get(b));
                }
                adapter.setBranchArrayList(selectedBranchArrayList);
                adapter.notifyDataSetChanged();

                Toast.makeText(BranchMenuActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0 &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    Intent intent = new Intent(BranchMenuActivity.this, MapsActivity.class); //map
                    intent.putExtra("branch", branchArrayList);
                    startActivity(intent);

                }
            }
        }
    }



    ValueEventListener newEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            branchArrayList = new ArrayList<>();
            DataSnapshot branches = snapshot.child("Branchs");
            User_Info userInfo = null;
            if(snapshot.child("Users").child(acc).exists()){
                userInfo = snapshot.child("Users").child(acc).child("userInfo").getValue(User_Info.class);
            }
            if(userInfo != null)
            {
                avatar=userInfo.getUserImage();
                adapter.setAvatar(avatar);
            }

            for (DataSnapshot tmp : branches.getChildren()) {
                //get branch id
                String id = tmp.getKey();
                String supID = (String) tmp.child("supermarketID").getValue();
                if (supermarketID.equals("-1") || supermarketID.equals(supID)) {
                    // get latlng
                    double[] latLng = new double[2];
                    double lat = (double) tmp.child("latLng").child("0").getValue();
                    double lng = (double) tmp.child("latLng").child("1").getValue();
                    latLng[0] = lat;
                    latLng[1] = lng;
                    //get add
                    String address = (String) tmp.child("address").getValue();
                    //get category
                    ArrayList<Integer> categories = new ArrayList<>();
                    for (DataSnapshot catID : tmp.child("category").getChildren()) {
                        categories.add(Integer.valueOf(catID.getKey()));
                    }
                    //get name
                    DataSnapshot supermarket = snapshot.child("Supermarkets").child(supID);
                    Supermarket tmpSup = supermarket.getValue(Supermarket.class);
                    Branch addBranch = new Branch(tmpSup, id, address, latLng, categories);
                    branchArrayList.add(addBranch);
                }
            }

            if(selectedBranchArrayList.isEmpty()) {
                adapter.setBranchArrayList(branchArrayList);
                adapter.notifyDataSetChanged();
            }
            //createView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        db.addValueEventListener(newEvent);
        cart = new Bundle();
        cart.putSerializable("orderItems",new ArrayList<Order_Item>());
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(newEvent);
    }

}