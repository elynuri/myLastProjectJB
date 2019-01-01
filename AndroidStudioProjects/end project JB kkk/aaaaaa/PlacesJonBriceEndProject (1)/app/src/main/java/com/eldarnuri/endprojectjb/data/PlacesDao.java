package com.eldarnuri.endprojectjb.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlacesDao {

    @Insert
    void insert(Places place_);

    @Delete
    void deleteNew(Places place_);


    @Query("DELETE FROM places_table")
    void deleteAll();

    @Query("DELETE FROM places_table WHERE name= :name_")
    void deleteByName(String name_);

    @Query("DELETE FROM places_table WHERE ID= :id_")
    void deleteByID(Long id_);

    @Query("SELECT * from places_table ORDER BY name ASC")
    LiveData<List<Places>> getAllPlaces();
}