package com.eldarnuri.endprojectjb.network;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.eldarnuri.endprojectjb.data.Places;
import com.eldarnuri.endprojectjb.interfaaaces.IPlacesDataReceived;
import com.eldarnuri.endprojectjb.models.LocationModel;
import com.eldarnuri.endprojectjb.repository.PlaceRepository;
import com.eldarnuri.endprojectjb.ui.MainActivity;
import com.eldarnuri.endprojectjb.ui.NearByApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkDataProvider {

    public static String TAG = "NetWorkDataProvider";
    double lat;
    double lng;

    public void getPlaceBySearchWord(String query, IPlacesDataReceived resultListener_){

        //go get data from google API
        // take time....
        //more time...
        //Data received -> resultListner_

        getPlaceBySearchWordAsyncTask getPlaceBySearchWordAsyncTask = new getPlaceBySearchWordAsyncTask(resultListener_);
        getPlaceBySearchWordAsyncTask.execute(query);
    }


    public void getPlacesByLocation(double lat_, double lng_, IPlacesDataReceived resultListener_) {

        //go get data from google API
        // take time....
        //more time...
        //Data received -> resultListner_
        lat = lat_;
        lng = lng_;
        getPlacesByLocationAsyncTask getPlacesByLocationAsyncTask = new getPlacesByLocationAsyncTask(resultListener_);
        getPlacesByLocationAsyncTask.execute(lat_, lng_);

    }

    private class getPlaceBySearchWordAsyncTask extends AsyncTask<String, Integer, IPlacesDataReceived>

    {

        private ArrayList<LocationModel> mLocationModels;
        private IPlacesDataReceived mIPlacesDataReceived;

        public getPlaceBySearchWordAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(String... strings) {


            String key = "AIzaSyDc7oNJ8FQZc6xmDVEvj-vewU5-ohnlwR0";
            String searchString = strings[0];


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(searchString)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!response.isSuccessful())
                try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            try {
                mLocationModels = getLocationListFromJson2(response.body().string());
                PlaceRepository placeRepository = new PlaceRepository(NearByApplication.getApplication());
                ArrayList<Places> listPlaces = new ArrayList<>();
                for (LocationModel locationModel : mLocationModels) {

                    Places place = new Places(locationModel.getName(), locationModel.getGeometry().getLocation().getLat(), locationModel.getGeometry().getLocation().getLng(), locationModel.getFormatted_address(), "", false, true);
                    listPlaces.add(place);
                }

                placeRepository.insert(listPlaces);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIPlacesDataReceived;
        }

        private ArrayList<LocationModel> getLocationListFromJson2(String jsonResponse) {
            List<LocationModel> stuLocationData = new ArrayList<LocationModel>();
            Gson gson = new GsonBuilder().create();
            getPlaceBySearchWordAsyncTask.LocationResponse response = gson.fromJson(jsonResponse, getPlaceBySearchWordAsyncTask.LocationResponse.class);
            stuLocationData = response.results;
            ArrayList<LocationModel> arrList = new ArrayList<>();
            arrList.addAll(stuLocationData);
            return arrList;
        }

        @Override
        protected void onPostExecute(IPlacesDataReceived iPlacesDataReceived_) {
            iPlacesDataReceived_.onPlacesDataReceived(mLocationModels);
        }


        public class LocationResponse {

            private List<LocationModel> results;


            public LocationResponse() {
                results = new ArrayList<>();
            }

        }
    }

    private class getPlacesByLocationAsyncTask extends AsyncTask<Double, Integer, IPlacesDataReceived>

    {
        private ArrayList<LocationModel> mLocationModels;

        private IPlacesDataReceived mIPlacesDataReceived;

        public getPlacesByLocationAsyncTask(IPlacesDataReceived iPlacesDataReceived) {
            mIPlacesDataReceived = iPlacesDataReceived;
        }

        @Override
        protected IPlacesDataReceived doInBackground(Double... doubles) {
            OkHttpClient client = new OkHttpClient();
            //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.029252,%2034.7425159&radius=1500&key=AIzaSyDc7oNJ8FQZc6xmDVEvj-vewU5-ohnlwR0
            String urlQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + doubles[0] + ",%20" + doubles[1] + "&radius=1500&key=AIzaSyDc7oNJ8FQZc6xmDVEvj-vewU5-ohnlwR0";
            Request request = new Request.Builder()
                    .url(urlQuery)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!response.isSuccessful())
                try {
                    throw new IOException("Unexpected code " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            try {
                mLocationModels = getLocationListFromJson(response.body().string());
                PlaceRepository placeRepository = new PlaceRepository(NearByApplication.getApplication());
                ArrayList<Places> listPlaces = new ArrayList<>();
                for (LocationModel locationModel : mLocationModels) {

                    Places place = new Places(locationModel.getName(), locationModel.getGeometry().getLocation().getLat(), locationModel.getGeometry().getLocation().getLng(), locationModel.getVicinity(), "", false, true);
                    listPlaces.add(place);
                }

                placeRepository.insert(listPlaces);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIPlacesDataReceived;
        }

        private ArrayList<LocationModel> getLocationListFromJson(String jsonResponse) {
            List<LocationModel> stuLocationData = new ArrayList<LocationModel>();
            Gson gson = new GsonBuilder().create();
            LocationResponse response = gson.fromJson(jsonResponse, LocationResponse.class);
            stuLocationData = response.results;
            ArrayList<LocationModel> arrList = new ArrayList<>();
            arrList.addAll(stuLocationData);
            return arrList;
        }

        @Override
        protected void onPostExecute(IPlacesDataReceived iPlacesDataReceived_) {
            iPlacesDataReceived_.onPlacesDataReceived(mLocationModels);
        }


        public class LocationResponse {

            private List<LocationModel> results;

            public LocationResponse() {
                results = new ArrayList<>();
            }

        }

    }
}
