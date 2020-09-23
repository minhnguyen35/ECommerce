package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

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
    Uri imageUri;
    ValueEventListener update;
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
        loadingBar = new ProgressDialog(this);
    }

    private ProgressDialog loadingBar;
    public void updateUser(final String id)
    {

        loadingBar.setTitle("Loading...");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final String[] downloadUri = new String[1];
        if(imageUri!=null) {
            final StorageReference savePath = storage.getReference().child("Users").child(userID).child(imageUri.getLastPathSegment() + ".jpg");
            final UploadTask uploadTask = savePath.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditUserInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditUserInfo.this, "Image Upload Successfully", Toast.LENGTH_LONG).show();
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                loadingBar.dismiss();
                                throw task.getException();

                            }
                            savePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri[0] = uri.toString();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(EditUserInfo.this,exception.getMessage(), Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                }
                            });;
                            return savePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                //Toast.makeText(EditUserInfo.this, downloadUri[0], Toast.LENGTH_LONG).show();
                                saveInfo(downloadUri[0]);
                            }
                        }
                    });
                }
            });
        }
        else{
            saveInfo(null);

        }

    }
    void saveInfo(final String downloadUri)
    {
        update = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(userID).exists()){
//                    User_Info oldUser = snapshot.child("Users").child(id).child("userInfo").getValue(User_Info.class);
//                    if(oldUser == null)
//                        return;
                    /*Update Old User Here*/
                    if (username.getText().length() > 0) userInfo.setUsername(username.getText().toString());
                    if (password.getText().length() > 0) userInfo.setPassword(password.getText().toString());
                    if (phone.getText().length() > 0) userInfo.setPhone(phone.getText().toString());
                    if (mail.getText().length() > 0) userInfo.setMail(mail.getText().toString());
                    if (bankNumber.getText().length() > 0) userInfo.setBankNumber(bankNumber.getText().toString());
                    if (address.getText().length() > 0) userInfo.setAddress(address.getText().toString());
                    if(downloadUri!=null)  userInfo.setUserImage(downloadUri);
                    db.child("Users").child(userID).child("userInfo").setValue(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CharSequence announce = "Update Successfully!";
                            Toast toast = Toast.makeText(EditUserInfo.this, announce, Toast.LENGTH_SHORT);
                            toast.show();
                            loadingBar.dismiss();
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(update);
        finish();
    }


    private void clickSave() {
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(userID);

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
                imageUri = data.getData();
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