package com.nerdery.umbrella.data.database.daos.base;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;
import java.util.List;

public interface BaseDao<T> {
  @Insert void insert(T... object);

  @Update void delete(T... object);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insert(List<T> object);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  void update(List<T> object);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  void update(T object);
}