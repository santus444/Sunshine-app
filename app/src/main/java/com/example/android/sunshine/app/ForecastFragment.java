package com.example.android.sunshine.app;

import android.app.LoaderManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.sunshine.app.data.WeatherContract;

import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ForecastAdapter mForecastAdapter;
    public String EXTRA_FORECAST = "com.example.android.sunshine.app.forecast";
    String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    private static final int FORECAST_LOADER = 0;
    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FORECAST_LOADER, this.ge);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateWeather();
            return true;

        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateWeather();
    }

    private void updateWeather(){
//        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        weatherTask.execute(preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default)), preferences.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_default)));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);
        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

//        List<String> weekForecast = new ArrayList<String>();
//        mForecastAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,weekForecast);

        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);
        lv.setAdapter(mForecastAdapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String forecast = mForecastAdapter.getItem(position);
////                Toast.makeText(view.getContext(), forecast, Toast.LENGTH_SHORT).show();
//                Intent detailsIntent = new Intent(getActivity(), DetailActivity.class);
//               // detailsIntent.putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(detailsIntent);
//
//
//            }
//        });
        Log.v(LOG_TAG,  "Fragment onCreateView called");


        return rootView;
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this.getActivity(), WeatherContract.WeatherEntry.CONTENT_URI, null,null,null, null);

    }

    void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
        mForecastAdapter.swapCursor(cursor);
    }

    void onLoaderReset(Loader<Cursor> cursorLoader){
        mForecastAdapter.swapCursor(null);
    }

}
