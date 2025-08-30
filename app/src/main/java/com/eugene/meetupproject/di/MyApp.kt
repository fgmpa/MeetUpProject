package com.eugene.meetupproject.di

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appComponent = DaggerAppComponent.factory().create(this)
    }
}