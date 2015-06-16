package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if(id == R.id.action_preferred_location_on_map){
            Intent mapIntent = new Intent(Intent.ACTION_VIEW);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String zip = preferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
            mapIntent.setData(Uri.parse("geo:0,0?q=" + zip));
            if(mapIntent.resolveActivity(this.getPackageManager()) != null ){
                startActivity(mapIntent);
            }else {
                Toast.makeText(this, "No Maps app is available", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
