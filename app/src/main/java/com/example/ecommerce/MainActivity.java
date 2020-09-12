package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        //initItemView();

        setUpLander();
        checkRemember();
    }
    private void checkRemember()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String isCheck = sharedPreferences.getString("remember", "");
        if(isCheck.equals("true"))
        {
            Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
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