package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.ecommerce.MainMenuActivity.acc;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager mlocationManager;
    private Location mLocation;

    private ArrayList<Branch> mBranchArrayList;
    private double[] from;

    private final String token = "pk.eyJ1IjoiY3M0MjYiLCJhIjoiY2tmYjY1cWkyMTJ2ZzMwbzc3czhveWFwYSJ9.hpBZHxDgg33rs9TnkeM6Kw";
    public static Polyline path=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        catchIntent();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mlocationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {

            String provider = mlocationManager.getBestProvider(new Criteria(),true);
            if(provider!=null)
                mlocationManager.requestLocationUpdates(provider,5000,5,mLocationListener);
        }

    }

    void catchIntent() {
        Intent intent = getIntent();
        mBranchArrayList = (ArrayList<Branch>) intent.getSerializableExtra("branch");
        from = intent.getDoubleArrayExtra("from");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        ImageView imageView = findViewById(R.id.imageTemp);
        for(int i=0; i<mBranchArrayList.size();++i){
            Picasso.get().load(Uri.parse(mBranchArrayList.get(i).getLogo())).into(imageView);
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mBranchArrayList.get(i).getLatLng()[0],mBranchArrayList.get(i).getLatLng()[1]))
                        .title(mBranchArrayList.get(i).getName())
                        .title("Chi nhánh: " + mBranchArrayList.get(i).getAddress())
                        .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap,120,100,false)))
                );
        }
        mMap.setOnMarkerClickListener(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mlocationManager!=null && mLocationListener!=null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                mlocationManager.removeUpdates(mLocationListener);
            }
        }
        mlocationManager=null;
        mLocationListener=null;
    }


    void requestApi(){
        if(from!=null && from.length==2){
            String myLat = String.valueOf(mLocation.getLatitude());
            String myLng = String.valueOf(mLocation.getLongitude());

            String desLat = String.valueOf(from[0]);
            String desLng = String.valueOf(from[1]);

            String src = myLng + ',' + myLat;
            String des = desLng + ',' + desLat;
            String location = src + ';' + des; //lat_src,lng_src;lat_des,lng_des


            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.mapbox.com")
                    .appendPath("directions")
                    .appendPath("v5")
                    .appendPath("mapbox")
                    .appendPath("cycling")
                    .appendPath(location)
                    .appendQueryParameter("geometries", "geojson")
                    .appendQueryParameter("access_token", token);


            String myUrl = builder.build().toString();
            boolean isConnected = false;
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new GetDirectionTask(mMap).execute(myUrl);
            }
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                mLocation=location;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15)
                        .bearing(90)
                        .tilt(30)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("You're here")
                        .icon(BitmapDescriptorFactory.fromBitmap(
                                Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeResource(getResources(),R.drawable.logo),
                                        120,
                                        120,
                                        false)
                                )
                        )
                );
                requestApi();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(from==null){
            from = new double[2];
        }
        from[0] = marker.getPosition().latitude;
        from[1] = marker.getPosition().longitude;
        requestApi();
        return false;
    }

}