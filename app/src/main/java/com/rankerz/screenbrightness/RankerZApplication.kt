package com.rankerz.screenbrightness

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RankerZApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialization code if needed
    }
}