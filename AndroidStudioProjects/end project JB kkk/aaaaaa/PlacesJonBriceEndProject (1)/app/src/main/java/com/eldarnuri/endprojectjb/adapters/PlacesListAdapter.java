package com.eldarnuri.endprojectjb.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eldarnuri.endprojectjb.data.Places;

import java.util.List;

import com.eldarnuri.endprojectjb.R;
import com.eldarnuri.endprojectjb.interfaaaces.GetInfoToMap;
import com.eldarnuri.endprojectjb.interfaaaces.IPlaceClicked;
import com.eldarnuri.endprojectjb.ui.FragmentA;
import com.eldarnuri.endprojectjb.ui.FragmentB;
import com.eldarnuri.endprojectjb.ui.MainActivity;
import com.eldarnuri.endprojectjb.ui.NearByApplication;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.PlaceViewHolder> implements GetInfoToMap{

    private static final String TAG = "PlaceListAdapter";

    private static IPlaceClicked placeClicked;
    private MainActivity main_;

    @Override
    public void onLocatioItemClick(Places newPlace) {
        main_ = MainActivity.getMyMain();
        main_.showClickedPlace(newPlace);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout parentLayout;
        //private ImageView mImage;
        private final TextView mPlaceName;
        private TextView mAddress;
        //private TextView mDistance;


        private PlaceViewHolder(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout_id);
            //mImage = itemView.findViewById(R.id.parent_layout_image_id);
            mPlaceName = itemView.findViewById(R.id.parent_layout_place_name_id);
            mAddress = itemView.findViewById(R.id.parent_layout_address_id);
            //mDistance = itemView.findViewById(R.id.parent_layout_distance_id);


        }
    }

    private final LayoutInflater mInflater;
    private List<Places> mPlacesList; // Cached copy of words
    private GetInfoToMap getInfoToMapInterface;

    public PlacesListAdapter(Context context) { mInflater = LayoutInflater.from(context); }


    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        if (mPlacesList != null) {
            final Places current = mPlacesList.get(position);
            holder.mPlaceName.setText(current.getName());
            holder.mAddress.setText(current.getAddress());

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + current.getName() );
                    Toast.makeText(mInflater.getContext(),"clicked on: " + current.getName(),Toast.LENGTH_LONG).show();
                    onLocatioItemClick(current);
                }
            });
                   } else {
            // Covers the case of data not being ready yet.
            holder.mPlaceName.setText("No Places");


        }



    }


    public void setPlaces(List<Places> places){
        mPlacesList = places;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlacesList != null)
            return mPlacesList.size();
        else return 0;
    }
}