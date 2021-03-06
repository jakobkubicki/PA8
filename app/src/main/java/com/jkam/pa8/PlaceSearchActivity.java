/**
 * This program creates Search for nerby restraunts
 * CPSC 312-01, Fall 2019
 * Programming Assignment #8
 * No sources to cite.
 *
 * @author Jakob Kubicci and Ahmad Moltafet
 * @version v1.0 12/10/21
 */



package com.jkam.pa8;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PlaceSearchActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> launcher;
    List<Place> results;
    String latL = "47.608013"; //Default to seattle coordinates in case user does not allow location
    String longL = "-122.335167";
    CustomAdapter adapter = new CustomAdapter();
    EditText search;
    String URL;
    String detailsRequest;
    String searchText = "";
    JSONObject jsonResponse = new JSONObject();
    String detailResponse;
    JSONObject jSONObject;
    JSONObject jSONObject2;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    /**
     Creates the place search
     *
     * @param savedInstanceState
=     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        results = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        search = findViewById(R.id.editTextSearch);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                    }
                });
    }

    /**
     Gets the last location
     *
     */
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latL = location.getLatitude() + "";
                            longL = location.getLongitude() + "";
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    /**
     Requests new location
     *
     */
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


    private LocationCallback mLocationCallback = new LocationCallback() {
        /**
         location results
         *
         */
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latL = mLastLocation.getLatitude() + "";
            longL = mLastLocation.getLongitude() + "";
        }
    };

    /**
     Checks location permissions
     *
     */
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     Requests permission for location
     *
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     Results of the location
     *
     */
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    /**
     Returns the list of places
     *
     */
    public void getDatabase(){
        results.clear();
        adapter.notifyDataSetChanged();
        searchText = search.getText().toString();
        System.out.println(searchText);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + searchText + "+&location=" + latL + "," + longL + "&radius=2000&region=us&type=restaurant&key=AIzaSyAVW_58WKYueAAOforWo7wgvdVXbF2UnZE";
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray locations = jsonResponse.getJSONArray("results");
                    for (int i = 0; i < 10; i++) {
                        jSONObject = locations.getJSONObject(i);
                        System.out.println(jSONObject);
                        String name = jSONObject.getString("name") + " (" + jSONObject.getString("rating") + ")";
                        String rating = jSONObject.getString("rating");
                        String address = jSONObject.getString("formatted_address");
                        String placeID = jSONObject.getString("place_id");
                        detailsRequest = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=AIzaSyAVW_58WKYueAAOforWo7wgvdVXbF2UnZE";
                        Place newPlace = new Place(name, address, "phone", false, rating);
                        getDetails(newPlace);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley Error " + error);
                Toast.makeText(PlaceSearchActivity.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError { }
        });
        queue.add(request);
    }

    /**
     Gets the details for each place
     *
     * @param newPlace object
     */
    public void getDetails(Place newPlace){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, detailsRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            detailResponse = response;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject locations = jsonObject.getJSONObject("result");
                                String phone = locations.getString("formatted_phone_number");
                                newPlace.setPhone(phone);
                                JSONObject opening_hours = locations.getJSONObject("opening_hours");
                                boolean open = opening_hours.getBoolean("open_now");
                                newPlace.setOpen(open);
                                String photo = locations.getString("icon");
                                newPlace.setURL(photo);
                                JSONArray reviews = locations.getJSONArray("reviews");
                                JSONObject review = reviews.getJSONObject(0);
                                newPlace.setReview(review.getString("text"));
                                System.out.println(phone);

                            }catch (JSONException err){
                                System.out.println("Error (get details)");
                            }
                            results.add(newPlace);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.location:
                getLastLocation();
                System.out.println(latL);
                System.out.println(longL);
                Toast.makeText(this, "get location", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.search:
                getDatabase();
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView myText1;
            TextView myText2;
            TextView myText3;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                myText1 = itemView.findViewById(R.id.myText1);
                myText2 = itemView.findViewById(R.id.address);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            /**
             Updates view for each part
             *
             * @param b to pass in
             */
            public void updateView(Place b) {
                myText1.setText(b.getName());
                myText2.setText(b.getAddress());
            }

            /**
             When user clicks
             *
             * @param v view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceSearchActivity.this, PlaceDetailActivity.class);
                intent.putExtra("index", getAdapterPosition());
                intent.putExtra("name", results.get(getAdapterPosition()).getName());
                intent.putExtra("address", results.get(getAdapterPosition()).getAddress());
                intent.putExtra("phone", results.get(getAdapterPosition()).getPhone());
                intent.putExtra("open_now", results.get(getAdapterPosition()).getOpen());
                intent.putExtra("url", results.get(getAdapterPosition()).getURL());
                intent.putExtra("review", results.get(getAdapterPosition()).getReview());
                launcher.launch(intent);
            }

            /**
             long click delete
             *
             * @param view to delete
             */
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        }

        /**
         Create the view
         *
         * @param parent, viewType
         * @return area of a circle
         */
        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PlaceSearchActivity.this)
                    .inflate(R.layout.card, parent, false);
            return new CustomViewHolder(view);
        }

        /**
         Updates the view for each card
         *
         * @param holder and position
         */
        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            Place b = results.get(position);
            holder.updateView(b);
        }

        /**
         Getter for the number of places
         *
         * @return number of places
         */
        @Override
        public int getItemCount() {
            return results.size();
        }
    }
}