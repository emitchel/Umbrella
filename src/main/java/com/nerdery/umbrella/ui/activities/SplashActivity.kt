package com.nerdery.umbrella.ui.activities

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.github.matteobattilana.weather.PrecipType.RAIN
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nerdery.umbrella.R
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.data.constants.ZipCodes
import com.nerdery.umbrella.data.services.ILocationService
import kotlinx.android.synthetic.main.activity_splash.iv_arrow_right
import kotlinx.android.synthetic.main.activity_splash.iv_umbrella
import kotlinx.android.synthetic.main.activity_splash.ll_continue_button
import kotlinx.android.synthetic.main.activity_splash.tv_owner
import kotlinx.android.synthetic.main.activity_splash.v_rain_blocker
import kotlinx.android.synthetic.main.activity_splash.wv_rain
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
  companion object {
    const val EMISSION_RATE = 500f
    const val FADE_OUT_PERCENTAGE = 1f
    const val ANIMATION_SPEED_MILLIS = 1000L
  }

  @Inject lateinit var sharedPreferences: SharedPreferences
  @Inject lateinit var locationService: ILocationService
  @Inject lateinit var eventBus: EventBus

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    UmbrellaApp.INSTANCE?.component?.inject(this)
    eventBus.register(this@SplashActivity)
    setContentView(R.layout.activity_splash)
    setupUI()
  }

  private fun setupUI() {
    setupWeatherAnimation()
    waitThenAnimate()
    ll_continue_button.setOnClickListener {
      Dexter.withActivity(this)
          .withPermissions(
              Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
          )
          .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
              if (report?.areAllPermissionsGranted() == true) {
                locationService.startLocationUpdates(true)
              } else {
                //defaulting to zip
                sharedPreferences.edit()
                    .putLong(SettingKeys.ZIP, ZipCodes.DEFAULT_ZIPCODE)
                    .apply()
              }
              //TODO: go to activity
            }

            override fun onPermissionRationaleShouldBeShown(
              permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
              token: PermissionToken?
            ) {
              token?.continuePermissionRequest()
            }

          })
          .check()
    }
  }

  private fun setupWeatherAnimation() {
    wv_rain.setWeatherData(RAIN)
    wv_rain.emissionRate = EMISSION_RATE
    wv_rain.fadeOutPercent = FADE_OUT_PERCENTAGE
    wv_rain.resetWeather()
  }

  private fun waitThenAnimate() {
    Handler().postDelayed({
      animateUmbrella()
    }, TimeUnit.SECONDS.toMillis(2))

    Handler().postDelayed({
      if (!sharedPreferences.getBoolean(SettingKeys.USER_ONBOARDED, false)) {
        ll_continue_button.animate()
            .alpha(1f)
            .setDuration(ANIMATION_SPEED_MILLIS)
            .setInterpolator(DecelerateInterpolator())
            .start()
        val bouncingAnimation =
          AnimationUtils.loadAnimation(this@SplashActivity, R.anim.bounce_to_right)
        iv_arrow_right.startAnimation(bouncingAnimation)
      } else {
        //TODO: go to homeActivity
      }
    }, TimeUnit.SECONDS.toMillis(4))
  }

  private fun animateOwner() {
    tv_owner.animate()
        .alpha(1f)
        .setDuration(ANIMATION_SPEED_MILLIS)
        .setInterpolator(DecelerateInterpolator())
        .start()
  }

  private fun animateUmbrella() {
    iv_umbrella.visibility = View.VISIBLE
    v_rain_blocker.visibility = View.VISIBLE
    val animation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.fly_up_from_bottom)
    animation.setAnimationListener(object : AnimationListener {

      override fun onAnimationEnd(animation: Animation?) {
        animateOwner()
      }

      override fun onAnimationRepeat(animation: Animation?) {
      }

      override fun onAnimationStart(animation: Animation?) {
      }

    })
    iv_umbrella.startAnimation(animation)
    v_rain_blocker.startAnimation(animation)
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onEvent(event: ILocationService.OnLocationUpdateEvent) {
    Timber.i("Got location, ${event.location}")
  }
}