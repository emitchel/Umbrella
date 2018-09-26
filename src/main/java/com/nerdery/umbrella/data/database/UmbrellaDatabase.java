package com.nerdery.umbrella.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.nerdery.umbrella.data.database.daos.ZipLocationDao;
import com.nerdery.umbrella.data.model.ZipLocation;

@Database(
    entities = {
        ZipLocation.class
    },
    version = 1,
    exportSchema = false)
public abstract class UmbrellaDatabase extends RoomDatabase {
  public static final String UMBRELLA_DATABASE = "umbrellaDatabase";
  private static UmbrellaDatabase INSTANCE;

  public abstract ZipLocationDao zipLocationDao();

  public synchronized static UmbrellaDatabase getAppDatabase(Context context) {
    if (INSTANCE == null) {
      INSTANCE =
          Room.databaseBuilder(context.getApplicationContext(), UmbrellaDatabase.class,
              UMBRELLA_DATABASE)
              .build();
    }
    return INSTANCE;
  }
}
