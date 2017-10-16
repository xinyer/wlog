package com.wx.log;


import android.app.Application;

import com.wx.lib.LogConfig;
import com.wx.lib.WLog;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogConfig config = new LogConfig.Builder()
                .intervalMillis(3600_000)
                .backupDays(15)
                .build();
        WLog.init(this, config);
    }
}
