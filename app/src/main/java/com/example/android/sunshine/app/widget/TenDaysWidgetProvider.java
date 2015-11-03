package com.example.android.sunshine.app.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.sync.SunshineSyncAdapter;

/**
 * Created by santosh on 10/17/15.
 */
public class TenDaysWidgetProvider extends AppWidgetProvider {
    String LOG_TAG = TenDaysWidgetProvider.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ten_days);

            // set intent for widget service that will create the views
            Intent serviceIntent = new Intent(context, DetailWidgetRemoteViewsService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            //   serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))); // embed extras so they don't get ignored
            remoteViews.setRemoteAdapter(appWidgetId, R.id.ten_days_listview, serviceIntent);
//            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.stackWidgetView, serviceIntent);
            remoteViews.setEmptyView(R.id.ten_days_listview, R.id.widget_listview_forecast_empty);

            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);

            remoteViews.setOnClickPendingIntent(R.id.widget_header, pendingIntent);
//            // set intent for item click (opens main activity)
//            Intent viewIntent = new Intent(context, HoneybuzzListActivity.class);
//            viewIntent.setAction(HoneybuzzListActivity.ACTION_VIEW);
//            viewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            viewIntent.setData(Uri.parse(viewIntent.toUri(Intent.URI_INTENT_SCHEME)));
//
//            PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
//            remoteViews.setPendingIntentTemplate(R.id.stackWidgetView, viewPendingIntent);

            // update widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(action)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);

            int[] ids = mgr.getAppWidgetIds(new ComponentName(context, TenDaysWidgetProvider.class));

            for (int id : ids) {
                mgr.notifyAppWidgetViewDataChanged(id, R.id.ten_days_listview);
            }
        }
    }
}
