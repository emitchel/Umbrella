package com.nerdery.umbrella.data.di

import com.nerdery.umbrella.ui.activities.SplashActivity
import dagger.Component

@UmbrellaScope
@Component(modules = [UmbrellaModule::class])
interface UmbrellaComponent {
  fun inject(splashActivity: SplashActivity)
}