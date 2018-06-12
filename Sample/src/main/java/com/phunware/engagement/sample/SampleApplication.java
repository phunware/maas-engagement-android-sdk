package com.phunware.engagement.sample;

import android.support.multidex.MultiDexApplication;

import com.phunware.core.PwCoreSession;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.EngagementLifecycleCallbacks;
import com.phunware.engagement.location.LocationManager;
import com.phunware.engagement.log.LogLogger;
import com.phunware.engagement.sample.loggers.ContentProviderLogger;

public class SampleApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        PwCoreSession.getInstance().setEnvironment(PwCoreSession.Environment.PROD);
        PwCoreSession.getInstance().registerKeys(this, getString(R.string.app_id),
                getString(R.string.access_key), getString(R.string.signature_key));

        new Engagement.Builder(this)
                .appId(Long.parseLong(getString(R.string.app_id)))
                .addLogger(new ContentProviderLogger(this))
                .addLogger(new LogLogger())
                .build();
        registerActivityLifecycleCallbacks(new EngagementLifecycleCallbacks());
    }

    public LocationManager getLocationManager() {
        return Engagement.locationManager();
    }

}
