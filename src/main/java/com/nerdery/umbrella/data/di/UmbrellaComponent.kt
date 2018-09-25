package com.nerdery.umbrella.data.di

import dagger.Component

@UmbrellaScope
@Component(modules = [UmbrellaModule::class])
interface UmbrellaComponent {

}