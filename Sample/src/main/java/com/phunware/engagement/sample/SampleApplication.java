package com.phunware.engagement.sample;

import android.app.Application;

import com.phunware.core.PwCoreSession;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.location.LocationManager;
import com.phunware.engagement.log.FileLogger;
import com.phunware.engagement.log.LogLogger;
import com.phunware.engagement.sample.loggers.ContentProviderLogger;

public class SampleApplication extends Application {

    public FileLogger mFileLogger;

    @Override
    public void onCreate() {
        super.onCreate();

        PwCoreSession.getInstance().setEnvironment(PwCoreSession.Environment.PROD);
        PwCoreSession.getInstance().registerKeys(this, getString(R.string.app_id),
                getString(R.string.access_key), getString(R.string.signature_key), "");

        mFileLogger = new FileLogger(this, Long.parseLong(getString(R.string.app_id)));

        new Engagement.Builder(this)
                .appId(Long.parseLong(getString(R.string.app_id)))
                .addLogger(new ContentProviderLogger(this))
                .addLogger(mFileLogger)
                .addLogger(new LogLogger())
                .build();
        Engagement.locationManager().start();
    }

    public LocationManager getLocationManager() { return Engagement.locationManager(); }


    public FileLogger getFileLogger() {
        return mFileLogger;
    }

}
