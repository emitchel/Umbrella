package com.nerdery.umbrella.data.di

import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.ui.activities.HomeActivity
import com.nerdery.umbrella.ui.activities.SplashActivity
import dagger.Component

@UmbrellaScope
@Component(modules = [UmbrellaModule::class])
interface UmbrellaComponent {
  fun inject(umbrellaApp: UmbrellaApp)
  fun inject(splashActivity: SplashActivity)
  fun inject(homeActivity: HomeActivity)
}