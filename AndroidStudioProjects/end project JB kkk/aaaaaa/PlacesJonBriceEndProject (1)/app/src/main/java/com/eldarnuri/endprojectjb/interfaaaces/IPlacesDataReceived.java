package com.eldarnuri.endprojectjb.interfaaaces;

import com.eldarnuri.endprojectjb.models.LocationModel;

import java.util.ArrayList;


public interface IPlacesDataReceived {

    public void onPlacesDataReceived(ArrayList<LocationModel> results_);
}
