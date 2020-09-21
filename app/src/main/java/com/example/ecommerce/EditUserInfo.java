package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditUserInfo extends AppCompatActivity {

    private User_Info userInfo;
    private Button BtnSave;
    private String userID;

    private boolean changeImage = false;
    private ImageView userImage;
    private EditText username;
    private EditText password;
    private EditText phone;
    private EditText mail;
    private EditText bankNumber;
    private EditText address;

    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        getData();
        initBtn();
        adaptHint();
        clickImage();
        clickSave();
    }

    private void adaptHint() {
        Picasso.get().load(userInfo.getUserImage()).fit().into(userImage);
        username.setHint(userInfo.getUsername());
        password.setHint(userInfo.getPassword());
        phone.setHint(userInfo.getPhone());
        mail.setHint(userInfo.getMail());
        bankNumber.setHint(userInfo.getBankNumber());
        address.setHint(userInfo.getAddress());
    }


    private void getData() {
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userInfo = (User_Info) intent.getSerializableExtra("userInfoCurrent");
    }

    private void initBtn() {
        BtnSave = findViewById(R.id.btn_save_info);

        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.userPass);
        phone = findViewById(R.id.userPhone);
        mail = findViewById(R.id.userMail);
        bankNumber = findViewById(R.id.userBank);
        address = findViewById(R.id.userAddress);
    }


    public void updateUser(final String id)
    {

        ValueEventListener update = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(id).exists()){
                    User_Info oldUser = snapshot.child("Users").child(id).child("userInfo").getValue(User_Info.class);
                    if(oldUser == null)
                        return;
                    /*Update Old User Here*/
                    if (username.getText().length() > 0) oldUser.setUsername(username.getText().toString());
                    if (password.getText().length() > 0) oldUser.setPassword(password.getText().toString());
                    if (phone.getText().length() > 0) oldUser.setPhone(phone.getText().toString());
                    if (mail.getText().length() > 0) oldUser.setMail(mail.getText().toString());
                    if (bankNumber.getText().length() > 0) oldUser.setBankNumber(bankNumber.getText().toString());
                    if (address.getText().length() > 0) oldUser.setAddress(address.getText().toString());

                    db.child("Users").child(id).child("userInfo").setValue(oldUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                CharSequence announce = "Update Successfully!";
                                Toast toast = Toast.makeText(EditUserInfo.this, announce, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                }
            }
            

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(update);

    }



    private void clickSave() {
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(userID);
                finish();
            }
        });
    }



    static final int GALLERY_REQUEST_CODE_SCAN = 12;
    private Button addImage;
    private Bitmap imageBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE_SCAN)
        {
            if(resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    private void addImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick Image"), GALLERY_REQUEST_CODE_SCAN);
    }

    private void clickImage()
    {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageFromGallery();
            }
        });
    }
}