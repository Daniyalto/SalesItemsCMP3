package org.example.project

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.example.project.Model.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SaleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Manually initialize Firebase (required for Compose Multiplatform)
        if (FirebaseApp.getApps(this).isEmpty()) {

            val options = FirebaseOptions.Builder()
                .setProjectId("salecmp-42d8b")
                .setApplicationId("1:59502853792:android:f4068fafb7e25363b213c5")
                .setApiKey("AIzaSyB4wO1z9-Lqaz8feiKOFoJf_f0Z-OPjAvg")
                .setStorageBucket("salecmp-42d8b.firebasestorage.app")
                .build()

            FirebaseApp.initializeApp(this, options)
        }

        // Start Koin DI
        startKoin {
            androidContext(this@SaleApp)
            modules(appModule)
        }
    }
}
