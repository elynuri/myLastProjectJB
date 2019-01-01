package com.eldarnuri.endprojectjb.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "places_table")
public class Places {


    public Places(@NonNull String mName, @NonNull Double mLat, @NonNull Double mLng, @NonNull String mAddress, String mPhoto, Boolean mIsLastSearch, Boolean mIsFavorite) {
        this.mName = mName;
        this.mLat = mLat;
        this.mLng = mLng;
        this.mAddress = mAddress;
        this.mPhoto = mPhoto;
        this.mIsLastSearch = mIsLastSearch;
        this.mIsFavorite = mIsFavorite;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private Long ID;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "lat")
    private Double mLat;


    @NonNull
    @ColumnInfo(name = "lng")
    private Double mLng;


    @NonNull
    @ColumnInfo(name = "address")
    private String mAddress;



    @ColumnInfo(name = "photo")
    private String mPhoto;

    @ColumnInfo(name = "isLastSearch")
    private Boolean mIsLastSearch;

    @ColumnInfo(name = "isFavorite")
    private Boolean mIsFavorite;


    @NonNull
    public Long getID() {
        return ID;
    }

    public void setID(@NonNull Long ID) {
        this.ID = ID;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String mName) {
        this.mName = mName;
    }

    @NonNull
    public Double getLat() {
        return mLat;
    }

    public void setLat(@NonNull Double mLat) {
        this.mLat = mLat;
    }

    @NonNull
    public Double getLng() {
        return mLng;
    }

    public void setLng(@NonNull Double mLng) {
        this.mLng = mLng;
    }

    @NonNull
    public String getAddress() {
        return mAddress;
    }

    public void setAddress(@NonNull String mAddress) {
        this.mAddress = mAddress;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public Boolean isIsLastSearch() {
        return mIsLastSearch;
    }

    public void setIsLastSearch(Boolean mIsLastSearch) {
        this.mIsLastSearch = mIsLastSearch;
    }

    public Boolean isIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(Boolean mIsFavorite) {
        this.mIsFavorite = mIsFavorite;
    }
}

