package org.aplie.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "OnCreate");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        String location = Utility.getPreferredLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q",location)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        }else{
            Log.d(LOG_TAG,"Couldn't call "+ location+", no");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "OnStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "OnResume");    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "OnPause");    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "OnStop");    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "OnDestroy");    }
}
