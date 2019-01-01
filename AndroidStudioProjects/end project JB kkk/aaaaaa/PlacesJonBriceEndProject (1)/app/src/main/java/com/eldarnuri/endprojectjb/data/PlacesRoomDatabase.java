package com.eldarnuri.endprojectjb.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Places.class}, version = 1, exportSchema = false)
public abstract  class PlacesRoomDatabase extends RoomDatabase {

    public abstract PlacesDao placesDao();

    private static volatile PlacesRoomDatabase INSTANCE;

       public static PlacesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlacesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PlacesRoomDatabase.class, "places_database")
                             .build();
                }
            }
        }
        return INSTANCE;
    }



}
