package com.nerdery.umbrella

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nerdery.umbrella.data.di.DaggerUmbrellaComponent
import com.nerdery.umbrella.data.di.UmbrellaComponent
import com.nerdery.umbrella.data.di.UmbrellaModule
import com.nerdery.umbrella.data.services.IZipCodeService
import timber.log.Timber
import javax.inject.Inject

class UmbrellaApp : Application() {

  @Inject lateinit var zipCodeService: IZipCodeService

  var component: UmbrellaComponent? = null

  companion object {
    var INSTANCE: UmbrellaApp? = null
  }

  override fun onCreate() {
    super.onCreate()
    INSTANCE = this
    setupDagger()
    setupStetho()
    setupTimber()
    AndroidThreeTen.init(this)
    zipCodeService.initDatabase(this)
  }

  fun setupTimber() {
    Timber.plant(object : Timber.DebugTree() {
      override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
      ) {
        super.log(priority, tag, message, t)
        if (priority == Log.ERROR) {
          //TODO event logging service
          //eventLoggingService.captureError(message)
        }
      }
    })
  }

  private fun setupDagger() {
    component = DaggerUmbrellaComponent.builder()
        .umbrellaModule(UmbrellaModule(this))
        .build()
    component?.inject(this)
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
