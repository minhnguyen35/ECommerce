package com.example.ecommerce;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.ecommerce.MapsActivity.path;

public class GetDirectionTask extends AsyncTask<String, Void, String> {

    ArrayList<ArrayList<Double>> listCoor = new ArrayList<>();
    private GoogleMap mMap;
    private Polyline oldPath = path;

    public GetDirectionTask(GoogleMap mMap)
    {
        this.mMap=mMap;
    }


    @Override
    protected String doInBackground(String... strings) {
        String res=readApi(strings[0]);
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        readJson(s);
        path=putPolyline(listCoor);
        if(oldPath!=null&&oldPath!=path) {
            oldPath.remove();
            oldPath=path;
        }
    }

    private String readApi(String s){
        StringBuilder builder=new StringBuilder();
        URL url;
        HttpURLConnection conn;
        InputStream inputStream;
        BufferedReader reader;
        try {
            url = new URL(s);
            conn =(HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line="";


            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            if (builder.length() == 0) {
                return null;
            }

            reader.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private ArrayList<ArrayList<Double>> readJson(String s){

        try {
            JSONObject object = new JSONObject(s);
            if(object.has("routes")) {
                JSONArray routes = object.getJSONArray("routes");
                for(int i=0; i<routes.length();++i)
                {
                    JSONObject element = routes.getJSONObject(i);
                    JSONObject geometry = element.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    for(int j=0;j<coordinates.length();++j) {
                        JSONArray corObj = (JSONArray) coordinates.get(j);
                        ArrayList<Double> corDouble = new ArrayList<>();

                        corDouble.add(corObj.getDouble(1));
                        corDouble.add(corObj.getDouble(0));
                        listCoor.add(corDouble);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listCoor;
    }

    private Polyline putPolyline(ArrayList<ArrayList<Double>> locs){
        PolylineOptions options = new PolylineOptions();
        for(int i=0;i<locs.size();++i){
            options.add(new LatLng(locs.get(i).get(0),locs.get(i).get(1)));
        }
        options.color(0xff0000ff);
        Polyline polyline = mMap.addPolyline(options);
        return polyline;
    }

}
