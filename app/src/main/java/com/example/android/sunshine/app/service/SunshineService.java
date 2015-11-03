package com.example.android.sunshine.app.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by santosh on 9/7/15.
 */
public class SunshineService extends IntentService {
    public static final String LOCATION_QUERY_EXTRA = "lqe";
    private final Context mContext;
    String LOG_TAG = SunshineService.class.getSimpleName();

    public SunshineService() {
        super("SunshineService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // This will only happen if there was an error getting or parsing the forecast.
    }


    public static class AlarmReceiver extends BroadcastReceiver {
        private final String LOG_TAG = AlarmReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, SunshineService.class);
            sendIntent.putExtra(SunshineService.LOCATION_QUERY_EXTRA, intent.getStringExtra(SunshineService.LOCATION_QUERY_EXTRA));
            context.startService(sendIntent);
        }
    }
}
