package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1355;
    private EditText name, quantity, price, description;
    RecyclerView recyclerViewArrayImage;
    private Button addImage, save;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog loadingBar;
    private Item editItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setUp();
        catchIntent();
        initialHint();
        getImage();
        saveClick();


    }
    public void updateItem()
    {
        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Loading...");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final List<String> downloadUri = new ArrayList<>();
        if(arrayUri.size() > 0) {

            for (int i = 0; i < arrayUri.size(); i++) {
                Uri imageUri = arrayUri.get(i);
                final StorageReference savePath = storage.getReference().child("Items").child(editItem.getID()).child(imageUri.getLastPathSegment() + ".jpg");
                final UploadTask uploadTask = savePath.putFile(imageUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

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
                                        downloadUri.add(uri.toString());
                                        Toast.makeText(AdminActivity.this, "Image URL", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Toast.makeText(AdminActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }
                                });
                                ;
                                return savePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(EditUserInfo.this, downloadUri[0], Toast.LENGTH_LONG).show();
                                    if (downloadUri.size() == arrayUri.size())
                                        saveInfo(downloadUri);
                                }
                            }
                        });
                    }
                });
            }
        }
        else{
            saveInfo(downloadUri);
        }


    }
    private void saveInfo(final List<String> uris)
    {
        ValueEventListener update = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;

                if(name.getText().length() > 0) {
                    editItem.setName(name.getText().toString());
                    flag = true;
                }
                if(price.getText().length() > 0) {
                    Long x = Long.valueOf(price.getText().toString());
                    editItem.setPrice(x);
                    flag = true;
                }
                if(quantity.getText().length()> 0)
                {
                    int x = Integer.parseInt((quantity.getText().toString()));
                    editItem.setQuantity(x);
                    flag = true;
                }
                if(description.getText().length() > 0)
                {
                    editItem.setDescription(description.getText().toString());
                    flag = true;
                }
                HashMap<String, Object> newItem = new HashMap<>();
                newItem.put("categoryID", editItem.getCategoryID());
                newItem.put("description", editItem.getDescription());
                newItem.put("price", editItem.getPrice());
                newItem.put("imageArrayList", uris);
                newItem.put("name", editItem.getName());
                newItem.put("quantity", editItem.getQuantity());
                newItem.put("id", editItem.getID());
                if(!flag && uris.size() == 0) return;
                db.child("Items").child(editItem.getID()).updateChildren(newItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingBar.dismiss();
                    }
                });
//                if(uris.size() >0){
//                for(int i = 0; i < uris.size(); i++)
//                {
//                    db.child("Items").child(editItem.getID()).child("imageArrayList").child(""+i).setValue(uris.get(i));
//                }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(update);


        finish();
    }

    private void saveClick() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
            }
        });
    }

    private void getImage()
    {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageFromGallery();

            }
        });
    }


    private void initialHint()
    {
        name.setHint(editItem.getName());
        //int priceIt = (int) editItem.getPrice();
        //price.setHint(priceIt);
        //quantity.setHint(editItem.getQuantity());
        description.setHint(editItem.getDescription());
    }


    ArrayList<Uri> arrayUri = new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST)
        {
            if(resultCode == RESULT_OK) {

                ClipData arrayImage = data.getClipData();
                if(arrayImage != null)
                {
                    for(int i = 0; i < arrayImage.getItemCount(); i++)
                    {
                        arrayUri.add(arrayImage.getItemAt(i).getUri());
                    }
                }
                else{

                    arrayUri.add(data.getData());
                }
                displayImage();
            }
        }

    }
    private void addImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select images"), GALLERY_REQUEST);

    }
    private void setUp() {
        recyclerViewArrayImage=findViewById(R.id.recyclerViewArrayImage);
        name = findViewById(R.id.name_product);
        quantity = findViewById(R.id.quantity_product);
        price = findViewById(R.id.price_product);
        description = findViewById(R.id.description_product);

        addImage = findViewById(R.id.add_image);

        save = findViewById(R.id.save_item);
    }

    private void displayImage()
    {
        recyclerViewArrayImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewArrayImage.setHasFixedSize(true);
        ArrayList<String> arrayString = new ArrayList<>();
        for(int i = 0; i < arrayUri.size(); i++)
            arrayString.add(arrayUri.get(i).toString());
        ImageAdapter imageAdapter = new ImageAdapter(this, arrayString);
        recyclerViewArrayImage.setAdapter(imageAdapter);
    }
    void catchIntent()
    {
        Intent intent = getIntent();

        editItem = (Item) intent.getSerializableExtra("ItemInfo");
        //categoryID = editItem.getCategoryID();

    }

}