package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initItemView();

        setUpLander();
        //checkRemember();
        testInsert();
    }

    private void testInsert() {
        Button button = findViewById(R.id.main_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = "anotheradmin";
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                ValueEventListener update = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User_Info getUser = new User_Info();
                        if(snapshot.child("Users").child(id).exists()){
                             getUser = snapshot.child("Users").child(id).child("userInfo").getValue(User_Info.class);
                        }
                        if(getUser != null)
                            Toast.makeText(MainActivity.this, getUser.getAddress(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                db.addListenerForSingleValueEvent(update);
            }
        });
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