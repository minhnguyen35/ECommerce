package com.example.ecommerce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LoadImageTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
    private ArrayList<Branch> mBranchArrayList;
    private GoogleMap mMap;

    public LoadImageTask(ArrayList<Branch> mBranchArrayList, GoogleMap mMap) {
        this.mBranchArrayList = mBranchArrayList;
        this.mMap = mMap;
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Void... strings) {
        ArrayList<Bitmap> ret=new ArrayList<>();
        for (int i = 0; i < mBranchArrayList.size(); ++i) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(mBranchArrayList.get(i).getLogo());
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ret.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ret;
    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
        if(mBranchArrayList.size()==0)
            return;
        int i = 0;
        for (; i < mBranchArrayList.size(); ++i) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mBranchArrayList.get(i).getLatLng()[0], mBranchArrayList.get(i).getLatLng()[1]))
                    .title(mBranchArrayList.get(i).getName())
                    .title("Chi nhánh: " + mBranchArrayList.get(i).getAddress())
                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmaps.get(i), 120, 100, false)))
            );
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mBranchArrayList.get(i-1).getLatLng()[0], mBranchArrayList.get(i-1).getLatLng()[1]))
                .zoom(15)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
