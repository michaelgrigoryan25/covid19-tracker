package com.michaelgrigoryan.covidtracker

import android.app.Application
import timber.log.Timber

/**
 * Created by Mbuodile Obiosio on Jan 03, 2021
 * @cazewonder
 * Nigeria.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}