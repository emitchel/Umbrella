package com.nerdery.umbrella.data.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import com.nerdery.umbrella.data.database.daos.base.BaseDao;
import com.nerdery.umbrella.data.model.ZipLocation;
import java.util.List;

@Dao
public abstract class ZipLocationDao implements BaseDao<ZipLocation> {
  @Query("SELECT * FROM ZipLocation")
  public abstract List<ZipLocation> getAll();

  @Query("DELETE FROM ZipLocation")
  public abstract void deleteAll();

  @Query("SELECT * FROM ZipLocation WHERE zipCode =:zipCode")
  public abstract ZipLocation getZipLocationByZip(long zipCode);

  @Transaction
  public void upsert(List<ZipLocation> zipLocations) {
    deleteAll();
    insert(zipLocations);
  }
}
