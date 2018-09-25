package com.nerdery.umbrella;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class UmbrellaApp extends Application {
  @Override public void onCreate() {
    super.onCreate();
    AndroidThreeTen.init(this);
  }
}
