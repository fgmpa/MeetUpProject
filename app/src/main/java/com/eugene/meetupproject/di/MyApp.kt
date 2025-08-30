package com.eugene.meetupproject.di

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true) // включает оффлайн режим
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
        FirebaseApp.initializeApp(this)
        appComponent = DaggerAppComponent.factory().create(this)
    }
}