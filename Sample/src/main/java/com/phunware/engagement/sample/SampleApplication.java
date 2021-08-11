package com.phunware.engagement.sample;

import androidx.multidex.MultiDexApplication;
import com.phunware.engagement.Engagement;

public class SampleApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Engagement.init(this);
        Engagement.enablePushNotifications();
    }

}
