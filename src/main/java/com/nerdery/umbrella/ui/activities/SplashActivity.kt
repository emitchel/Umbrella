package com.nerdery.umbrella.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.github.matteobattilana.weather.PrecipType.RAIN
import com.nerdery.umbrella.R
import com.nerdery.umbrella.R.anim
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.ui.activities.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.iv_arrow_right
import kotlinx.android.synthetic.main.activity_splash.iv_umbrella
import kotlinx.android.synthetic.main.activity_splash.ll_continue_button
import kotlinx.android.synthetic.main.activity_splash.tv_owner
import kotlinx.android.synthetic.main.activity_splash.v_rain_blocker
import kotlinx.android.synthetic.main.activity_splash.wv_rain
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class SplashActivity : BaseActivity() {
  companion object {
    const val EMISSION_RATE = 500f
    const val FADE_OUT_PERCENTAGE = 1f
    const val ANIMATION_SPEED_MILLIS = 1000L
  }

  @Inject lateinit var sharedPreferences: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    UmbrellaApp.INSTANCE?.component?.inject(this)
    setContentView(R.layout.activity_splash)
    setupUI()
  }

  private fun setupUI() {
    setupWeatherAnimation()
    waitThenAnimate()
    ll_continue_button.setOnClickListener {
      goToHomeActivity()
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

  }

  fun animateNextSteps() {
    Handler().postDelayed({
      if (!sharedPreferences.getBoolean(SettingKeys.USER_ONBOARDED, false)) {
        ll_continue_button.animate()
            .alpha(1f)
            .setDuration(ANIMATION_SPEED_MILLIS)
            .setInterpolator(DecelerateInterpolator())
            .start()
        val bouncingAnimation =
          AnimationUtils.loadAnimation(this@SplashActivity, anim.bounce_to_right)
        iv_arrow_right.startAnimation(bouncingAnimation)
      } else {
        goToHomeActivity()
      }
    }, SECONDS.toMillis(2))
  }

  private fun goToHomeActivity() {
    startActivity(Intent(this, HomeActivity::class.java))
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
        animateNextSteps()
      }

      override fun onAnimationRepeat(animation: Animation?) {
      }

      override fun onAnimationStart(animation: Animation?) {
      }

    })
    iv_umbrella.startAnimation(animation)
    v_rain_blocker.startAnimation(animation)
  }
}