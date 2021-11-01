package com.learn.codingchallenge.core

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Rafiqul Hasan
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
    }
}