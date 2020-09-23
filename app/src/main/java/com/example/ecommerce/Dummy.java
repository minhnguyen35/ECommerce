package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Dummy extends AppCompatActivity {
    private Button Logout;
    private EditText name, address, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        Logout = findViewById(R.id.LogOut);
        logOutAccount();
    }
    private void logOutAccount()//log out day nha
    {
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("checkbox", Context.MODE_PRIVATE); // cai nay la de bo cai ghi nho dang nhap
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("remember", "false");
                editor.apply();
                Intent intent = new Intent(Dummy.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }

}