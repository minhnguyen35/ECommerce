package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminAddItemActivity extends AppCompatActivity {


    static final int GALLERY_REQUEST = 155;
    private EditText name, quantity, price, description, category;
    RecyclerView recyclerViewArrayImage;
    private Button addImage, save;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog loadingBar;
    String newID;
    String newCat;
    private Item editItem;
    private String branchID;
    private ValueEventListener newIdListener;
    private boolean isNewCate = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_item);
        setUp();

        Intent intent = getIntent();
        branchID = intent.getStringExtra("branchID");


        getImage();
        saveClick();


    }
    private void newIDCat()
    {
        newIdListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tmpID;
                tmpID = snapshot.child("ID").child("ItemID").getValue().toString();
                int newtmpid = Integer.valueOf(tmpID) + 1;
                newID = String.valueOf(newtmpid);



                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingBar.setTitle("Loading...");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        newIDCat();
        db.addListenerForSingleValueEvent(newIdListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.removeEventListener(newIdListener);
    }

    public void updateItem()
    {
        loadingBar.setTitle("Loading...");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final List<String> downloadUri = new ArrayList<>();
        if(arrayUri.size() > 0) {
            for (int i = 0; i < arrayUri.size(); i++) {
                Uri imageUri = arrayUri.get(i);
                final StorageReference savePath = storage.getReference().child("Items").child(newID).child(imageUri.getLastPathSegment() + ".jpg");
                final UploadTask uploadTask = savePath.putFile(imageUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminAddItemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(AdminAddItemActivity.this, "Image URL", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Toast.makeText(AdminAddItemActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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
                String itemName = " ", itemDesc = " ";
                long itemPrice = 1000, itemQuan = 0;
                if(name.getText().length() > 10) {
                    itemName = name.getText().toString();
                }
                else{
                    name.setError("Item Name Length > 10 Characters");
                    name.requestFocus();
                }
                if(price.getText().length() > 3) {
                   itemPrice = Long.valueOf(price.getText().toString());
                }
                else{
                    price.setError("Item Name Length >= 1000");
                    price.requestFocus();
                }
                if(quantity.getText().length()> 0)
                {
                    itemQuan = Integer.parseInt((quantity.getText().toString()));

                }
                else{
                    quantity.setError("Please Input Quantity");
                    quantity.requestFocus();
                }
                if(description.getText().length() > 20)
                {
                    itemDesc = description.getText().toString();
                }
                else{
                    description.setError("Please add valid description!");
                    description.requestFocus();
                }
                HashMap<String, Object> newItem = new HashMap<>();
                isCatExists(snapshot);
                newItem.put("categoryID", newCat);
                newItem.put("description", itemDesc);
                newItem.put("price", itemPrice);
                if(uris.size() > 0)
                    newItem.put("imageArrayList", uris);
                newItem.put("name", itemName);
                newItem.put("quantity", itemQuan);
                newItem.put("id", newID);

                if(isNewCate)
                {
                    String newCate = category.getText().toString();
                    HashMap<String, Object> newCategoryAdd = new HashMap<>();
                    newCategoryAdd.put("id", newCat);
                    newCategoryAdd.put("branchID", branchID);
                    newCategoryAdd.put("name", newCate);
                    db.child("ID").child("CategoryID").setValue(newCat);
                    db.child("Category").child(newCat).updateChildren(newCategoryAdd).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminAddItemActivity.this, "Category Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                db.child("ID").child("ItemID").setValue(newID);

                db.child("Items").child(newID).updateChildren(newItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(update);
        loadingBar.dismiss();
        finish();
    }
    private void isCatExists(DataSnapshot snapshot)
    {
        String newCate = category.getText().toString();
        for(DataSnapshot x: snapshot.child("Category").getChildren()){
            String current = x.child("branchID").getValue().toString();
            String cateName = x.child("name").getValue().toString();
            if(current.equals(branchID) && cateName.equals(newCate))
            {
                isNewCate = false;
                newCat = x.child("id").getValue().toString();
                break;
            }
        }
        if(isNewCate)
        {
            String tmpC;
            tmpC = snapshot.child("ID").child("CategoryID").getValue().toString();
            int newtmpid = Integer.valueOf(tmpC) + 1;
            newCat = String.valueOf(newtmpid);
        }
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
        loadingBar = new ProgressDialog(this);
        recyclerViewArrayImage=findViewById(R.id.recyclerViewArrayImage);
        name = findViewById(R.id.name_product);
        quantity = findViewById(R.id.quantity_product);
        price = findViewById(R.id.price_product);
        description = findViewById(R.id.description_product);
        addImage = findViewById(R.id.add_image);
        category = findViewById(R.id.CategoryAdd);
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

}