package com.example.android.sunshine.app.sync;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.sunshine.app.ForecastFragment;
import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.widget.TodayWidgetProvider;

/**
 * Created by santosh on 10/11/15.
 */
public class TodayWidgetIntentService extends IntentService {
    private String LOG_TAG = TodayWidgetIntentService.class.getSimpleName();
    private String mLocation;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TodayWidgetIntentService() {
        super("TodayWidgetIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = this;
//        for (int appWidgetId : intent.getIntArrayExtra(TodayWidgetProvider.SUNSHINE_WIDGET_IDS)) {

//            RemoteViews views = new RemoteViews(
//                    this.getPackageName(),
//                    R.layout.widget_today_small);
//            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
        Bundle extras = intent.getExtras();
        int[] appWidgetIds = new int[0];
        if (extras != null) {
            appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        } else {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, TodayWidgetProvider.class));
        }
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_today_small);
            //int appWidgetId = intent.getIntExtra(TodayWidgetProvider.SUNSHINE_WIDGET_IDS, 0);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (Build.VERSION.SDK_INT >= 16) {
                Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
                int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
                Log.v(LOG_TAG, "OPTION_APPWIDGET_MIN_WIDTH = " + width);
                if (width >= 220) {
                    views = new RemoteViews(
                            context.getPackageName(),
                            R.layout.widget_today_large);
                    Log.v(LOG_TAG, "Called widget_today");
                } else if (width > 110) {
                    views = new RemoteViews(
                            context.getPackageName(),
                            R.layout.widget_today);
                    Log.v(LOG_TAG, "Called widget_today");
                } else if (width <= 110) {
                    views = new RemoteViews(
                            context.getPackageName(),
                            R.layout.widget_today_small);
                    Log.v(LOG_TAG, "Called widget_today_small");

                }

            }


            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            Log.v(LOG_TAG, "I am in TodayWidgetIntentService onHandle");
            mLocation = Utility.getPreferredLocation(context);
            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

            Uri baseUri;
            baseUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                    mLocation, System.currentTimeMillis());
            Cursor cursor = context.getContentResolver().query(baseUri, ForecastFragment.FORECAST_COLUMNS, null, null, sortOrder);

            if (cursor.moveToFirst()) {
                float highTemp = cursor.getFloat(ForecastFragment.COL_WEATHER_MAX_TEMP);
                float lowTemp = cursor.getFloat(ForecastFragment.COL_WEATHER_MIN_TEMP);
                String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);

                views.setTextViewText(R.id.list_item_high_textview, Utility.formatTemperature(context, highTemp));
                views.setTextViewText(R.id.list_item_low_textview, Utility.formatTemperature(context, lowTemp));
                views.setTextViewText(R.id.list_item_forecast_textview, description);

                int weatherCondition = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
//            Glide.with(context)
//                    .load(Utility.getArtResourceUrlForWeatherCondition(context, weatherCondition))
//                    .error(Utility.getArtResourceForWeatherCondition(weatherCondition)).into();
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);

//        }
        }
    }
}
