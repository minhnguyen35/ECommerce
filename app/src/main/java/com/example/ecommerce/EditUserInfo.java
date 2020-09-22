package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static com.google.android.material.internal.ViewUtils.dpToPx;

public class EditUserInfo extends AppCompatActivity {

    private User_Info userInfo;
    private Button BtnSave;
    private String userID;

    private boolean changeImage = false;
    private ImageView userImage;
    private TextView username;
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
        username.setText(userInfo.getUsername());
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
    ValueEventListener update;
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
                    //if (username.getText().length() > 0) userInfo.setUsername(username.getText().toString());
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
    public void updateUser(final String id)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final String[] downloadUri = new String[1];
        if(imageUri!=null) {
            final StorageReference savePath = storage.getReference().child("Users").child(userID).child(imageUri.getLastPathSegment() + ".jpg");
            final UploadTask uploadTask = savePath.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditUserInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditUserInfo.this, "Image Upload Successfully", Toast.LENGTH_LONG).show();
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
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



    private void clickSave() {
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(userID);
               /* try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                finish();
            }
        });
    }



    static final int GALLERY_REQUEST_CODE_SCAN = 12;
    //private Button addImage;
    private Bitmap imageBitmap;
    Uri imageUri;
   /* @Override
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

                scaleImage(imageBitmap);
                //userImage.setImageBitmap(imageBitmap);

            }
        }

    }
*/
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
                addScaleImage();
                //addImageFromGallery();
                //Picasso.get().load(userInfo.getUserImage()).fit().into(userImage);
            }
        });
    }

   private void addScaleImage() {
        // start cropping activity for pre-acquired image saved on the device
       CropImage.activity(imageUri)
               .setAspectRatio(1, 1)
               .start(this);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            userImage.setImageURI(imageUri);
        }
        else if (requestCode != RESULT_OK){
            Toast.makeText(this, "Load image fail !", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(EditUserInfo.this, EditUserInfo.class));
            //finish();
        }
        else {
            Toast.makeText(this, "Load and crop image fail !", Toast.LENGTH_LONG).show();
        }
    }
}