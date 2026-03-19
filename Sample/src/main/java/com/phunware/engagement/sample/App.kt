package com.phunware.engagement.sample

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.phunware.engagement.Engagement.Companion.locationManager
import com.phunware.engagement.location.LocationManager

internal class App : Application() {

    val locationManager: LocationManager
        get() = locationManager()

    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(applicationContext, Renderer.LATEST) {/* no-op */ }
    }
}