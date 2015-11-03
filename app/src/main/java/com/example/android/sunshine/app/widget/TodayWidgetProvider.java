package com.example.android.sunshine.app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.sunshine.app.sync.SunshineSyncAdapter;
import com.example.android.sunshine.app.sync.TodayWidgetIntentService;

/**
 * Created by santosh on 10/8/15.
 */
public class TodayWidgetProvider extends AppWidgetProvider {

    public static final String SUNSHINE_WIDGET_IDS = "swids";
    private String LOG_TAG = TodayWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for (int appWidgetId : appWidgetIds){
        Intent intentForTodayWidget = new Intent(context, TodayWidgetIntentService.class);
        intentForTodayWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(intentForTodayWidget);
        Log.v(LOG_TAG, "I am in onUpdate");
//        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(action)) {
//            Bundle extras = intent.getExtras();
//            int[] appWidgetIds = new int[0];
//            if (extras != null) {
//                appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//            }
//            for (int appWidgetId : appWidgetIds) {

            Intent intentForTodayWidget = new Intent(context, TodayWidgetIntentService.class);
            context.startService(intentForTodayWidget);
            Log.v(LOG_TAG, "I am in onReceive");

            // }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.v(LOG_TAG, "I am in changed");
        Intent intentForTodayWidget = new Intent(context, TodayWidgetIntentService.class);
        intentForTodayWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        context.startService(intentForTodayWidget);
    }
}
