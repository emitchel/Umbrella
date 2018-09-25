package com.nerdery.umbrella

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nerdery.umbrella.data.di.DaggerUmbrellaComponent
import com.nerdery.umbrella.data.di.UmbrellaComponent

class UmbrellaApp : Application() {
  var component: UmbrellaComponent? = null
  var instance: UmbrellaApp? = null

  override fun onCreate() {
    super.onCreate()
    instance = this
    setupDagger()
    setupStetho()
    AndroidThreeTen.init(this)
  }

  private fun setupDagger() {
    component = DaggerUmbrellaComponent.builder()
        .build()
  }

  private fun setupStetho() {
    // Create an InitializerBuilder
    val initializerBuilder = Stetho.newInitializerBuilder(this)

    // Enable command line interface
    initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this))

    // Enable Chrome DevTools with Realm
    initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))

    // Use the InitializerBuilder to generate an Initializer
    val initializer = initializerBuilder.build()

    // Initialize Stetho with the Initializer
    Stetho.initialize(initializer)
  }
}
