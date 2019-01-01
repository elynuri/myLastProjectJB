package com.eldarnuri.endprojectjb.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.eldarnuri.endprojectjb.data.Places;
import com.eldarnuri.endprojectjb.data.PlacesDao;
import com.eldarnuri.endprojectjb.data.PlacesRoomDatabase;

import java.util.List;



public class PlaceRepository {

    private PlacesDao mPLacesDao;
    private LiveData<List<Places>> mAllPlaces;

    public PlaceRepository(Application application) {
        PlacesRoomDatabase db = PlacesRoomDatabase.getDatabase(application);
        mPLacesDao = db.placesDao();
        mAllPlaces = mPLacesDao.getAllPlaces();
    }




    public LiveData<List<Places>> getAllPlaces() {
        return mAllPlaces;
    }

    public void insert (Places place_) {
        new insertAsyncTask(mPLacesDao).execute(place_);
    }

    public void insert (List<Places> placeList_) {
        for(Places p :placeList_) {
           insert(p);
        }
    }


    private static class insertAsyncTask extends AsyncTask<Places, Void, Void> {

        private PlacesDao mAsyncTaskDao;

        insertAsyncTask(PlacesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Places... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deletAllPlaces(){
        new deleteAllAsyncTask(mPLacesDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Places, Void, Void> {

        private PlacesDao mAsyncTaskDao;

        deleteAllAsyncTask(PlacesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Places... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
