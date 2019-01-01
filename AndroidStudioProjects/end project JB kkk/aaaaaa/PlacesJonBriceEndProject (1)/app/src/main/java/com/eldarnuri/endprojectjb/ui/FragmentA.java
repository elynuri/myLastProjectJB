package com.eldarnuri.endprojectjb.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eldarnuri.endprojectjb.R;


public class FragmentA extends Fragment {

    private static final String TAG = "FragmentA";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: initializing... ");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false);

    }

}
