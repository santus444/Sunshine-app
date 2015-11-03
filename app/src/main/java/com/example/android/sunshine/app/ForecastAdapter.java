package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext, high, isMetric) + "/" + Utility.formatTemperature(mContext, low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        if (viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today;
        } else if (viewType == VIEW_TYPE_FUTURE_DAY) {
            layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        MainActivityViewHolder mainActivityViewHolder = new MainActivityViewHolder(view);
        view.setTag(mainActivityViewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        MainActivityViewHolder mainActivityViewHolder = (MainActivityViewHolder) view.getTag();
        int weatherCondition = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        int viewType = getItemViewType(cursor.getPosition());
        if (viewType == VIEW_TYPE_TODAY) {
            //mainActivityViewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherCondition));
            Glide.with(context)
                    .load(Utility.getArtResourceUrlForWeatherCondition(context, weatherCondition))
                    .error(Utility.getArtResourceForWeatherCondition(weatherCondition))
                    .into(mainActivityViewHolder.iconView);
        } else if (viewType == VIEW_TYPE_FUTURE_DAY) {
            //mainActivityViewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherCondition));
            Glide.with(context)
                    .load(Utility.getIconResourceUrlForWeatherCondition(context, weatherCondition))
                    .error(Utility.getIconResourceForWeatherCondition(weatherCondition))
                    .into(mainActivityViewHolder.iconView);
        }

        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        mainActivityViewHolder.descriptionView.setText(description);
        mainActivityViewHolder.descriptionView.setContentDescription("Weather condition looks " + description);

        mainActivityViewHolder.iconView.setContentDescription("Weather condition looks " + description);


        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        mainActivityViewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));
        mainActivityViewHolder.dateView.setContentDescription(Utility.getFriendlyDayString(context, dateInMillis));

        boolean isMetric = Utility.isMetric(context);

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        mainActivityViewHolder.highTempView.setText(Utility.formatTemperature(context, high, isMetric));
        mainActivityViewHolder.highTempView.setContentDescription("temperature at high is " + Utility.formatTemperature(context, high, isMetric));

        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        mainActivityViewHolder.lowTempView.setText(Utility.formatTemperature(context, low, isMetric));
        mainActivityViewHolder.lowTempView.setContentDescription("temperature at low is " + Utility.formatTemperature(context, low, isMetric));
    }
}