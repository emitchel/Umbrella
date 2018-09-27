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

  @Query("SELECT * FROM ZipLocation WHERE (latitude BETWEEN :latMin AND :latMax) AND (longitude BETWEEN :longMin and :longMax)")
  public abstract List<ZipLocation> getZipLocationInRange(double latMin, double latMax,
      double longMin, double longMax);

  @Query("SELECT * FROM ZIPLOCATION WHERE latitude = :latitude and longitude = :longitutde")
  public abstract List<ZipLocation> getZipByLatLong(double latitude, double longitutde);

  @Query("SELECT * FROM ZipLocation WHERE zipCode =:zipCode")
  public abstract ZipLocation getZipLocationByZip(long zipCode);

  @Transaction
  public void upsert(List<ZipLocation> zipLocations) {
    deleteAll();
    insert(zipLocations);
  }
}
