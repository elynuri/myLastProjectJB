package com.eldarnuri.endprojectjb.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.eldarnuri.endprojectjb.data.Places;
import com.eldarnuri.endprojectjb.repository.PlaceRepository;

import java.util.List;



public class PlaceViewModel extends AndroidViewModel {

    private PlaceRepository mRepository;

    private LiveData<List<Places>> mAllPlaces;

    public PlaceViewModel(Application application) {
        super(application);
        mRepository = new PlaceRepository(application);
        mAllPlaces = mRepository.getAllPlaces();
    }

    public LiveData<List<Places>> getAllPlaces() { return mAllPlaces; }

    public void insert(Places word) { mRepository.insert(word); }

    public void deleteAllPlaces(){
        mRepository.deletAllPlaces();
    }
}