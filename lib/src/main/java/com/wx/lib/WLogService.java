package com.wx.lib;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.joda.time.LocalDateTime;


public class WLogService extends IntentService {

    private static final String TAG = "WLogService";

    public WLogService() {
        super("WLogService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent:" + LocalDateTime.now());
        if (intent != null) {
            Log.d(TAG, "timing action upload log.");
            WLog.writeLogCat(this);
            WLog.processBackup(this);
        }
    }
}
