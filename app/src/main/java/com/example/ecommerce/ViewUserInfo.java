package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

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
    private Button BtnCheck;
    private Dialog PopUpCheck;
    private EditText passCheck;

    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ValueEventListener user;

    private final int REQUEST_CODE_CART = 10000, REQUEST_CODE_ACCOUNT = 20000, REQUEST_CODE_ORDERS = 30000;
    private final int REQUEST_CODE_ITEM = 789;
    private final int RESULT_LOGOUT = 88888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_info);

        getData();
        initBtn();
        clickEdit();
    }

    private void getData() {
        Intent getAccount = getIntent();
        userID = getAccount.getStringExtra("account");
    }

    private void initBtn() {
        BtnEdit = (Button) findViewById(R.id.btn_edit_info);

        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.userPass);
        phone = findViewById(R.id.userPhone);
        mail = findViewById(R.id.userMail);
        bankNumber = findViewById(R.id.userBank);
        address = findViewById(R.id.userAddress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //final String id = "anotheradmin";

        getDataFromDB(userID);
    }

    private void getDataFromDB(final String account) {
        user = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //User_Info getUser = new User_Info();
                if(snapshot.child("Users").child(account).exists()){
                    userInfo = snapshot.child("Users").child(account).child("userInfo").getValue(User_Info.class);
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
        db.addValueEventListener(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(user);
    }




    private void adaptInfo() {
        if (userInfo.getUserImage().contains("https://firebasestorage.googleapis.com/") == true) Picasso.get().load(userInfo.getUserImage()).fit().into(userImage);
        username.setText(userInfo.getUsername());
        //password.setText(userInfo.getPassword());
        password.setText("*******");
        phone.setText(userInfo.getPhone());
        mail.setText(userInfo.getMail());
        bankNumber.setText(userInfo.getBankNumber());
        address.setText(userInfo.getAddress());
    }

    private void scaleImage(Bitmap imageBitmap) {
        if (imageBitmap == null) return;

        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();

        float boundingX = userImage.getWidth();
        float boundingY = userImage.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = boundingX / width;
        float yScale = boundingY / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, width, height, matrix, true);
        userImage.setImageBitmap(scaledBitmap);

    }

    private void clickEdit() {
        BtnEdit = (Button) findViewById(R.id.btn_edit_info);
        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyPassword();
            }
        });
    }

    private void verifyPassword() {
        PopUpCheck = new Dialog(this);
        PopUpCheck.setContentView(R.layout.dialog_check_password);
        passCheck = PopUpCheck.findViewById(R.id.passCheck);
        BtnCheck = PopUpCheck.findViewById(R.id.btn_check_pass);

        BtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpCheck.dismiss();
                if (passCheck.getText().toString().compareTo(userInfo.getPassword()) == 0) viewEditInfo();
                else Toast.makeText(ViewUserInfo.this, "Incorrect password !", Toast.LENGTH_SHORT).show();
            }
        });

        PopUpCheck.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopUpCheck.show();

    }

    private void viewEditInfo() {
        Intent intent2 = new Intent(ViewUserInfo.this, EditUserInfo.class);
        intent2.putExtra("userID", userID);
        intent2.putExtra("userInfoCurrent", userInfo);
        startActivity(intent2);
        //startActivityForResult(intent2, 1);
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