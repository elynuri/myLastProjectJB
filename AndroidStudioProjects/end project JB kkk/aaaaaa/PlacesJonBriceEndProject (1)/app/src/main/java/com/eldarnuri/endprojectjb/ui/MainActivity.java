package com.eldarnuri.endprojectjb.ui;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.eldarnuri.endprojectjb.adapters.PlacesListAdapter;
import com.eldarnuri.endprojectjb.data.Places;
import com.eldarnuri.endprojectjb.interfaaaces.GetInfoToMap;
import com.eldarnuri.endprojectjb.interfaaaces.IPlaceClicked;
import com.eldarnuri.endprojectjb.interfaaaces.IPlacesDataReceived;
import com.eldarnuri.endprojectjb.model.PlaceViewModel;
import com.eldarnuri.endprojectjb.models.LocationModel;
import com.eldarnuri.endprojectjb.network.NetWorkDataProvider;

import java.util.ArrayList;
import java.util.List;

import com.eldarnuri.endprojectjb.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity implements IPlacesDataReceived, IPlaceClicked {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private PlaceViewModel mPlacesViewModel;

    public static FragmentB myFragmentB;
    public static FragmentA myFragmentA;
    public static FragmentC myFragmentC;
    public static MainActivity myMain;


    //PowerConnectionReceiver powerConnectionReceiver;
    //BroadcastReceiver powerConnectionReceiver;

    // get location vars
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLocation;

    // location permission vars
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 8657;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        myFragmentA = new FragmentA();
        myFragmentB = new FragmentB();
        myFragmentC=new FragmentC();
        setMain_(this);

        //check if google service is ok than initializing map
        if (isServicesOK()) {
            init();
        }


//        NetWorkDataProvider dataProvider = new NetWorkDataProvider();
//            dataProvider.getPlacesByLocation(32.093240, 34.852270, this);
//            dataProvider.getPlacesByLocation(latLng.latitude, latLng.longitude, this);


        mPlacesViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);

        // Use to delete the list of places in FragmentA
        mPlacesViewModel.deleteAllPlaces();


        // Register PowerConnectionReceiver
//        powerConnectionReceiver = new PowerConnectionReceiver();
//        Intent intent = this.registerReceiver(powerConnectionReceiver,
//                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // Tool Bar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.app_bar_id);
        setSupportActionBar(toolbar);


    }


    private void init() {
        // get location
        getMyLocation();
    }

    // The Interface IPlacesDataReceived action
    @Override
    public void onPlacesDataReceived(ArrayList<LocationModel> results_) {
        // pass data result to adapter
        final PlacesListAdapter adapter = new PlacesListAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.places_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPlacesViewModel.getAllPlaces().observe(this, new Observer<List<Places>>() {
            @Override
            public void onChanged(@Nullable final List<Places> places) {
                // Update the cached copy of the places in the adapter.
                adapter.setPlaces(places);
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map request
            Log.d(TAG, "isServicesOK: google play services is working");
            //Toast.makeText(MainActivity.this,"google play services is working",Toast.LENGTH_LONG).show();

            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an ERROR occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an ERROR occurred but we can resolve it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(MainActivity.this, "you can't make map requests", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //
    private void getMyLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        String[] permission = {FINE_LOCATION, COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSIONS_REQUEST_CODE);

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            //Toast.makeText(MainActivity.this,"location is not null",Toast.LENGTH_LONG).show();

                            NetWorkDataProvider dataProvider = new NetWorkDataProvider();
                            dataProvider.getPlacesByLocation(location.getLatitude(), location.getLongitude(), MainActivity.this);
                            currentLocation = new Location("");
                            currentLocation = location;
                        }
                    }
                });
    }


    //menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        android.support.v7.widget.SearchView searchView = findViewById(R.id.search_view_id);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mPlacesViewModel.deleteAllPlaces();
                String key = "AIzaSyDc7oNJ8FQZc6xmDVEvj-vewU5-ohnlwR0";
                //mSearchWord = "https://maps.googleapis.com/maps/api/place/textsearch/json?input="+query+"&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&locationbias=circle:50000@"+currentLocation.getLatitude()+","+currentLocation.getLongitude()+"&key="+key;
                // "https://maps.googleapis.com/maps/api/place/textsearch/json?query=BBB&location=32.029252,%2034.7425159&radius=10000&key=AIzaSyDc7oNJ8FQZc6xmDVEvj-vewU5-ohnlwR0";
                // https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query+"&location="+currentLocation.getLatitude()+","+currentLocation.getLongitude()+"&radius=10000&key="+key
                String sss = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + query + "&location=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&radius=10000&key=" + key;
                NetWorkDataProvider dataProvider = new NetWorkDataProvider();
                dataProvider.getPlaceBySearchWord(sss, MainActivity.this);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Fragment fragment;

        switch (item.getItemId()) {

            case R.id.action_search:
//                fragment = new FragmentA();
//                FragmentTransaction fmSearch = getSupportFragmentManager().beginTransaction();
//                fmSearch.replace(R.id.fragment_main, fragment);
//                fmSearch.commit();

                return true;

            case R.id.action_map:
//                fragment = new FragmentB();
//                FragmentTransaction fmMap = getSupportFragmentManager().beginTransaction();
//                fmMap.replace(R.id.fragment_main, fragment);
//                fmMap.commit();

                return true;

            case R.id.action_favorites:
                return true;

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showClickedPlace(Places myPlace) {
        myFragmentB.setClickedPlace(myPlace);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_main)!=null) {
            transaction.replace(R.id.fragment_main, myFragmentB);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            transaction.replace(R.id.fragment_b_id,myFragmentC);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void setMain_(MainActivity main_) {
        myMain = main_;
    }

    public static MainActivity getMyMain() {
        return myMain;
    }

}
