package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.xml.sax.DTDHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetItem implements FirebaseGetBehaviour {
    private Item item;
    private Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<Uri> x;
    ArrayList<Item> list;
    public void getAllSupermarket()
    {
        final ArrayList<Supermarket> listSupermarket = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Supermarkets");
                for(DataSnapshot it: itemList.getChildren())
                {
                    String id = it.getKey();
                    Supermarket supermarket = itemList.getValue(Supermarket.class);
                    listSupermarket.add(supermarket);

                }
                  /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getBranch(final String supermarketID)
    {
        final ArrayList<Branch> listBranch = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot branches = snapshot.child("Branchs");
                for(DataSnapshot tmp: branches.getChildren())
                {
                    //get branch id
                    String id = tmp.getKey();
                    String supID = (String) tmp.child("supermarketID").getValue();
                    if(!supermarketID.equals("-1"))
                    {
                        if(supermarketID.equals(supID))
                        {
                            // get latlng
                            double[] latLng = new double[2];
                            double lat = (double)tmp.child("latLng").child("0").getValue();
                            double lng = (double)tmp.child("latLng").child("1").getValue();
                            latLng[0] = lat; latLng[1] = lng;
                            //get add
                            String address = (String)tmp.child("address").getValue();
                            //get category
                            ArrayList<Integer> categories = new ArrayList<>();
                            for(DataSnapshot catID: tmp.child("category").getChildren()) {
                                categories.add((Integer) catID.getValue());
                            }

                            //get name
                            DataSnapshot supermarket = snapshot.child("Supermarkets").child(supID);
                            Supermarket tmpSup = supermarket.getValue(Supermarket.class);
                            Branch addBranch = new Branch(tmpSup, id, address, latLng,categories);
                            listBranch.add(addBranch);
                        }
                    }
                    else {

                        // get latlng
                        double[] latLng = new double[2];
                        double lat = (double) tmp.child("latLng").child("0").getValue();
                        double lng = (double) tmp.child("latLng").child("1").getValue();
                        latLng[0] = lat;
                        latLng[1] = lng;
                        //get add
                        String address = (String) tmp.child(id).child("address").getValue();
                        //get category
                        ArrayList<Integer> categories = new ArrayList<>();
                        for (DataSnapshot catID : tmp.child("category").getChildren()) {
                            categories.add(Integer.valueOf(catID.getKey()));
                        }
                        //get name
                        DataSnapshot supermarket = snapshot.child("Supermarkets").child(supID);
                        Supermarket tmpSup = supermarket.getValue(Supermarket.class);
                        Branch addBranch = new Branch(tmpSup, id, address, latLng, categories);
                        listBranch.add(addBranch);
                    }
                }
                //Toast.makeText(context, String.valueOf(listBranch.size()), Toast.LENGTH_LONG).show();
                  /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getOrder(final String UserID)
    {
        final ArrayList<User_Order> listOrder = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot orders = snapshot.child("Orders");
                for(DataSnapshot tmp: orders.getChildren())
                {
                    //get branch id
                    String account = (String) tmp.child("account").getValue();
                    if(UserID.equals(account))
                    {
                        User_Order orderCur = tmp.getValue(User_Order.class);
                    }
                }
               //Toast.makeText(MainActivity.this, String.valueOf(listBranch.size()), Toast.LENGTH_LONG).show();
                  /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getOrderItem(final String OrderID)
    {
        final ArrayList<User_Order> listOrder = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot orders = snapshot.child("OrderItem");
                for(DataSnapshot tmp: orders.getChildren())
                {
                    //get branch id
                    String orderID = (String) tmp.child("orderID").getValue();
                    if(OrderID.equals(orderID))
                    {
                        Order_Item orderItemCur = tmp.getValue(Order_Item.class);
                    }
                }
                //Toast.makeText(MainActivity.this, String.valueOf(listBranch.size()), Toast.LENGTH_LONG).show();
                  /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getBranchCond(final String supID, final String add)
    {
        final ArrayList<Branch> listBranch = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot branches = snapshot.child("Branchs");
                for(DataSnapshot tmp: branches.getChildren())
                {
                    //get branch id
                    String id = tmp.getKey();
                    String supermarketID = (String) tmp.child("supermarketID").getValue();
                    if(supID.equals(supermarketID))
                    {
                        //get add
                        String address = (String)tmp.child("address").getValue();
                        add.toLowerCase();
                        address.toLowerCase();
                        if(address.contains(add)) {
                            // get latlng
                            double[] latLng = new double[2];
                            double lat = (double) tmp.child("latLng").child("0").getValue();
                            double lng = (double) tmp.child("latLng").child("1").getValue();
                            latLng[0] = lat;
                            latLng[1] = lng;
                            //get category
                            ArrayList<Integer> categories = new ArrayList<>();
                            for (DataSnapshot catID : tmp.child("category").getChildren()) {
                                categories.add(Integer.valueOf(catID.getKey()));
                            }

                            //get name
                            DataSnapshot supermarket = snapshot.child("Supermarkets").child(supID);
                            Supermarket tmpSup = supermarket.getValue(Supermarket.class);
                            Branch addBranch = new Branch(tmpSup, id, address, latLng, categories);
                            listBranch.add(addBranch);
                        }
                    }


                }
//                        Toast.makeText(MainActivity.this, String.valueOf(listBranch.size()), Toast.LENGTH_LONG).show();
                  /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*
    public void updateUser(final String id)
    {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Uri imageUri = "images/";
        String acc = "anotheradmin";
        final String[] downloadUri = new String[1];
        final StorageReference savePath = storage.getReference().child("Users").child(acc).child(imageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = savePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Image Upload Successfully", Toast.LENGTH_LONG).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadUri[0] = savePath.getDownloadUrl().toString();
                        return savePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(context, "Successful Update!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ValueEventListener update = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(id).exists()){
                    User_Info oldUser = snapshot.child("Users").child(id).child("userInfo").getValue(User_Info.class);
                    if(oldUser == null)
                        return;
                    String newAddress = "Newyork city";
                    oldUser.setAddress(newAddress);
                    db.child("Users").child(id).child("userInfo").setValue(oldUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                CharSequence announce = "Update Successfully!";
                                Toast toast = Toast.makeText(context, announce, Toast.LENGTH_SHORT);
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
    */
    public GetItem(Context context) {
        this.context = context;
    }
    /*public void getCategory(final String branchID)
    {
        final ArrayList<Item> listItem = new ArrayList<>();
        final ArrayList<Category> listCategory = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Category");
                for(DataSnapshot it: itemList.getChildren())
                {
                    String brID = it.child("branchID").getValue().toString();
                    if(branchID.equals(brID)) {
                        Category newCate;
                        String ID = it.child("id").getValue().toString();
                        String name = it.child("name").getValue().toString();
                        ArrayList<String> items = new ArrayList<>();
                        for(DataSnapshot x: it.child("items").getChildren())
                        {
                            String id = x.getValue().toString();
                            items.add(id);
                        }
                        newCate = new Category(ID, name,items, brID);
                        listCategory.add(newCate);

                    }
                }


                    Do something with the data

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
    public void getList(final String categoryID)
    {

        final ArrayList<Item> listItems = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Items");
                for(DataSnapshot it: itemList.getChildren())
                {
                    String catID = it.child("categoryID").getValue().toString();
                    if(categoryID.equals(catID)) {
                        String ID = it.child("id").getValue().toString();
                        String Name= it.child("name").getValue().toString();
                        long Price = it.child("price").getValue(Long.class);
                        ArrayList<String> imageUrl = new ArrayList<>();
                        for(DataSnapshot url : it.child("imageArrayList").getChildren())
                        {
                            String curUrl = url.getValue().toString();
                            imageUrl.add(curUrl);
                        }
                        int Quantity = it.child("quantity").getValue(Long.class).intValue();
                        String Description= (String) it.child("description").getValue();
                        Item newItem = new Item(ID, catID, Name, Price, imageUrl, Quantity, Description);
                        listItems.add(newItem);
                    }
                }
                    /*
                    Do something with the data
                    */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getList()
    {

    }
    @Override
    public void getData(final String id) {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Items");
                if(itemList.child(id).exists())
                {
                    String ID = itemList.child(id).getValue().toString();
                    String CategoryID = itemList.child(id).child("categoryID").getValue().toString();

                    String Name= itemList.child(id).child("name").getValue().toString();
                    long Price = itemList.child(id).child("price").getValue(Long.class);

                    int Quantity = itemList.child(id).child("quantity").getValue(Long.class).intValue();
                    String Description= (String) itemList.child(id).child("description").getValue();
                    item = new Item(ID, CategoryID, Name, Price, null, Quantity, Description);

                    /*Do something with this item*/
                }
                else{
                    CharSequence s = "Item out of stock";
                    Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    ValueEventListener add;
    public void addOrder(final User_Order newOrder)
    {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        add =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int tmpOrdID = (int) snapshot.child("ID").child("OrderID").getValue() + 1;
                final String newOrdID = String.valueOf(tmpOrdID);
                newOrder.setId(newOrdID);
                db.child("Orders").child(newOrdID).setValue(newOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.child("ID").child("OrderID").setValue(newOrdID).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Success Add Order", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(add);
    }
    public void checkValidItems(final ArrayList<Order_Item> listOrderItems, final ArrayList<Integer> listQuantityInDB)
    {
        //final ArrayList<Order_Item> listOrderItems;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        ValueEventListener checkItems = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot items = snapshot.child("Items");
                for(int i = 0; i < listOrderItems.size(); i++)
                {
                    Order_Item curItem = listOrderItems.get(i);
                    String id = curItem.getId();
                    if(!items.child(id).exists())
                    {
                        Toast.makeText(context, "Item " + id + " is out of stock!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int quantityOrd = curItem.getQuantity();
                    long quantityRealtmp = (long) items.child(id).child("quantity").getValue();
                    int quantityReal = (int) quantityRealtmp;
                    listQuantityInDB.add(quantityReal);
                    if(quantityOrd > quantityReal)
                    {
                        Toast.makeText(context, "Item " + id + " is out of stock!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long priceOrd = curItem.getPrice();
                    long priceReal = (long) items.child(id).child("price").getValue();
                    if(priceOrd != priceReal)
                    {
                        Toast.makeText(context, "Item " + id + " is out of stock!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        db.addListenerForSingleValueEvent(checkItems);
    }
/*
    public void updateItem(final ArrayList<Order_Item> orderItems)
    {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        DatabaseReference itemObject = db.child("Items");
        ArrayList<Order_Item> listOrderItems;
        User_Order newOrd;
        if(itemObject != null)
        {
            itemObject.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    boolean isAbort = false;
                    ArrayList<Integer> listQuantityInDB = new ArrayList<>();
                    for(int i = 0; i < orderItems.size(); i++)
                    {
                        Order_Item newItem = orderItems.get(i);
                        int quantityOrd = newItem.getQuantity();
                        int quantityReal = (int) currentData.child(newItem.getId()).child("quantity").getValue();
                        listQuantityInDB.add(quantityReal);
                        if(quantityOrd > quantityReal)
                        {
                            Toast.makeText(context, "Quantity is not enough for the item " +
                                    newItem.getName(), Toast.LENGTH_LONG).show();
                            return Transaction.abort();
                        }
                        long priceOrd = newItem.getPrice();
                        long priceReal = (long) currentData.child(newItem.getID()).child("price").getValue();
                        if(priceOrd != priceReal)
                        {
                            Toast.makeText(context, "Sorry there's something wrong with Item "
                                    + newItem.getName(), Toast.LENGTH_LONG).show();
                            return Transaction.abort();
                        }
                    }


                    for(int i = 0; i < orderItems.size();i++)
                    {
                        String id = orderItems.get(i).getID();
                        int quantityOrd = orderItems.get(i).getQuantity();
                        int quantityReal = listQuantityInDB.get(i);
                        HashMap<String, Object> itemAfter = new HashMap<>();
                        itemAfter.put("quantity", quantityReal - quantityOrd);
                        db.child("Items").child(id).updateChildren(itemAfter);


                    }
                    return  Transaction.success(currentData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed,
                                       @Nullable DataSnapshot currentData) {

                    Toast.makeText(context, "Success Transaction", Toast.LENGTH_SHORT).show();
                }

            });
        }

    }
    */
    public void getOrderItem(final ArrayList<String> items)
    {

        final ArrayList<Item> listOrderItem = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot itemList = snapshot.child("Items");
                for(int i = 0; i <items.size(); i++) {
                    String id = items.get(i);

                    if (itemList.child(id).exists()) {
                        String ID = itemList.child(id).getValue().toString();
                        String CategoryID = itemList.child(id).child("categoryID").getValue().toString();

                        String Name= itemList.child(id).child("name").getValue().toString();
                        long Price = itemList.child(id).child("price").getValue(Long.class);

                        int Quantity = itemList.child(id).child("quantity").getValue(Long.class).intValue();
                        String Description= (String) itemList.child(id).child("description").getValue();
                        ArrayList<String> tmp = new ArrayList<>();
                        for(DataSnapshot x: itemList.child(id).child("imageArrayList").getChildren())
                            tmp.add(x.getValue().toString());
                        Item addItem= new Item(ID, CategoryID, Name, Price, null, Quantity, Description);

                        listOrderItem.add(addItem);

                    } else {
                        CharSequence s = "Item out of stock";
                        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loadImageItem(String id) {

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final List<String> x = new ArrayList<>();
        final StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("Item").child("1238");
        final List<String> paths = new ArrayList<>();
        pathReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.

                            paths.add(item.getPath());
                        }
                        for(int i = 0; i < paths.size(); i++) {
                            StorageReference item = storageRef.child(paths.get(i));
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    x.add(uri.toString());
                                    if(x.size() == paths.size())
                                    {
                                        /*display image*/

                                    }

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    void doTransaction(String id)
    {

    }
}
