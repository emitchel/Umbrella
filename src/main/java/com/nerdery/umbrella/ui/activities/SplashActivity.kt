package com.nerdery.umbrella.ui.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.github.matteobattilana.weather.PrecipType.RAIN
import com.nerdery.umbrella.R
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.services.IZipCodeService
import kotlinx.android.synthetic.main.activity_main.iv_umbrella
import kotlinx.android.synthetic.main.activity_main.tv_owner
import kotlinx.android.synthetic.main.activity_main.v_rain_blocker
import kotlinx.android.synthetic.main.activity_main.wv_rain
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
  companion object {
    const val EMISSION_RATE = 500f
    const val FADE_OUT_PERCENTAGE = 1f
    const val ANIMATION_SPEED_MILLIS = 2000L
  }

  @Inject lateinit var zipCodeService: IZipCodeService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    UmbrellaApp.instance?.component?.inject(this)
    setContentView(R.layout.activity_main)
    setupUI()
  }

  private fun setupUI() {
    setupWeatherAnimation()
    waitThenAnimate()
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
      animateOwner()
    }, TimeUnit.SECONDS.toMillis(2))

    //TODO: if first run, show continue button, otherwise continue to homepage
    Handler().postDelayed({
      //TODO: show continue button and animate arrow
    }, TimeUnit.SECONDS.toMillis(3))
  }

  private fun animateOwner() {
    tv_owner.visibility = View.VISIBLE
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
    iv_umbrella.startAnimation(animation)
    v_rain_blocker.startAnimation(animation)
  }
}