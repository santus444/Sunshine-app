package com.example.android.sunshine.app.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.sunshine.app.ForecastFragment;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by santosh on 10/17/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WeatherWidgetRemoteViewsFactory(DetailWidgetRemoteViewsService.this, intent);
    }

    class WeatherWidgetRemoteViewsFactory implements RemoteViewsFactory {
        private final String LOG_TAG = WeatherWidgetRemoteViewsFactory.class.getSimpleName();
        private Context mContext;
        private Cursor mCursor;
        private int mAppWidgetId;
        private String mLocation;

        public WeatherWidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.v(LOG_TAG, "10 days app widget id: " + mAppWidgetId);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }
            mLocation = Utility.getPreferredLocation(mContext);

            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

            Uri baseUri;
            baseUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                    mLocation, System.currentTimeMillis());
            final long token = Binder.clearCallingIdentity();
            try {
                mCursor = mContext.getContentResolver().query(baseUri, ForecastFragment.FORECAST_COLUMNS, null, null, sortOrder);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_forecast);
            if (mCursor.moveToPosition(position)) {
                String highTemp = Utility.formatTemperature(mContext, mCursor.getFloat(ForecastFragment.COL_WEATHER_MAX_TEMP));
                String lowTemp = Utility.formatTemperature(mContext, mCursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
                remoteViews.setTextViewText(R.id.list_item_high_textview, highTemp);
                remoteViews.setTextViewText(R.id.list_item_low_textview, lowTemp);
            }
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
