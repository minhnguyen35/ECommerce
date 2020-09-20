package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ViewUserInfo extends AppCompatActivity {

    private User_Info userInfo;
    private Button BtnEdit;
    private String userID;

    private ImageView userImage;
    private TextView username;
    private TextView password;
    private TextView phone;
    private TextView mail;
    private TextView bankNumber;
    private TextView address;
    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ValueEventListener user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_info);

        getData();
        initBtn();
        clickEdit();
    }

    private void getData() {
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
    }

    private void initBtn() {
        BtnEdit.findViewById(R.id.btn_edit_info);

        userImage.findViewById(R.id.userImage);
        username.findViewById(R.id.userName);
        password.findViewById(R.id.userPass);
        phone.findViewById(R.id.userPhone);
        mail.findViewById(R.id.userMail);
        bankNumber.findViewById(R.id.userBank);
        address.findViewById(R.id.userAddress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //final String id = "anotheradmin";

        getDataFromDB();
    }

    private void getDataFromDB() {
        user = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //User_Info getUser = new User_Info();
                if(snapshot.child("Users").child(userID).exists()){
                    userInfo = snapshot.child("Users").child(userID).child("userInfo").getValue(User_Info.class);
                }
                if(userInfo != null)
                {
                    adaptInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(user);
    }




    private void adaptInfo() {
        Picasso.get().load(userInfo.getUserImage()).fit().into(userImage);
        username.setText(userInfo.getUsername());
        password.setText(userInfo.getPassword());
        phone.setText(userInfo.getPhone());
        mail.setText(userInfo.getMail());
        bankNumber.setText(userInfo.getBankNumber());
        address.setText(userInfo.getAddress());
    }

    private void clickEdit() {
        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ViewUserInfo.this, EditUserInfo.class);
                intent2.putExtra("userID", userID);
                intent2.putExtra("userInfoCurrent", userInfo);
                startActivity(intent2);
                //startActivityForResult(intent2, 1);
            }
        });
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
           /User_Info tmp = (User_Info) data.getSerializableExtra("newUserInfo");
            userInfo.setUsername(tmp.getUsername());
            userInfo.setPassword(tmp.getPassword());
            userInfo.setPhone(tmp.getPhone());
            userInfo.setMail(tmp.getMail());
            userInfo.setBankNumber(tmp.getBankNumber());
            userInfo.setAddress(tmp.getAddress());

            adaptInfo();
        }
    }*/

}