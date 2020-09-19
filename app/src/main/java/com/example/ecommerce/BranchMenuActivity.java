package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BranchMenuActivity extends AppCompatActivity {
    ArrayList<Branch> branchArrayList;
    ArrayList<Branch> displaySupermarketBranches;

    FloatingActionButton fab;
    Spinner spinner;
    RecyclerView recyclerView;

    BranchAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_menu);

        catchIntent();
        mapping();
        createView();
        createListener();
    }

    void mapping(){
        spinner = findViewById(R.id.spinnerCity);
        recyclerView = findViewById(R.id.recyclerViewBranches);
        fab = findViewById(R.id.fabLocation);
    }

    void catchIntent(){
        Intent intent = getIntent();
        branchArrayList = (ArrayList<Branch>) intent.getSerializableExtra("branch");
        //displaySupermarketBranches.addAll(branchArrayList);
    }

    void createView(){
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new BranchAdapter(this, branchArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    void createListener(){
        spinner.setOnItemSelectedListener(spinnerHelper);
        fab.setOnClickListener(fabHelper);
    }

    FloatingActionButton.OnClickListener fabHelper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(BranchMenuActivity.this, MapsActivity.class); //map
            intent.putExtra("branch", branchArrayList);
            startActivity(intent);
        }
    };

    Spinner.OnItemSelectedListener spinnerHelper = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            /*displaySupermarketBranches.clear();
            for (int b = 0; b < branchArrayList.size(); ++b) {
                if (branchArrayList.get(b).getAddress().contains(spinner.getSelectedItem().toString()))
                    displaySupermarketBranches.add(branchArrayList.get(b));
            }
            adapter.notifyDataSetChanged();
*/
            Toast.makeText(BranchMenuActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}