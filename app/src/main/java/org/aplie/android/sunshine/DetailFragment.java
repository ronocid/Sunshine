package org.aplie.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.aplie.android.sunshine.data.WeatherContract;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LOADER_ID_DETAILS = 501;
    static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private ImageView imageWeather;
    private TextView tvForecast;
    private TextView tvPressure;
    private TextView tvWind;
    private TextView tvHumidity;
    private TextView tvLow;
    private TextView tvHight;
    private TextView tvDate;
    private TextView tvDay;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_WEATHER_HUMDITY = 5;
    static final int COL_WEATHER_PRESSURE = 6;
    static final int COL_WEATHER_WIND_SPEED = 7;
    static final int COL_WEATHER_DEGRESS = 8;
    static final int COL_WEATHER_CONDITION_ID = 9;
    private Uri mUri;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args != null){
            mUri = args.getParcelable(DetailFragment.DETAIL_URI);
        }
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        imageWeather = (ImageView)view.findViewById(R.id.fragment_detail_icon);
        tvDay = (TextView) view.findViewById(R.id.fragment_detail_day_textview);
        tvDate = (TextView) view.findViewById(R.id.fragment_detail_date_textview);
        tvForecast = (TextView) view.findViewById(R.id.fragment_detail_forecast_textview);
        tvHight = (TextView) view.findViewById(R.id.fragment_detail_high_textview);
        tvLow = (TextView) view.findViewById(R.id.fragment_detail_low_textview);
        tvHumidity = (TextView) view.findViewById(R.id.fragment_detail_humidity_textview);
        tvWind = (TextView) view.findViewById(R.id.fragment_detail_wind_textview);
        tvPressure = (TextView) view.findViewById(R.id.fragment_detail_pressure_textview);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if(mForecast != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        setShareIntent(intent);
        return intent;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(null == mUri){
            return null;
        }

        return new CursorLoader(getActivity(),mUri, FORECAST_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loadData(data);
    }

    private void loadData(Cursor data) {
        if(!data.moveToFirst()){
            return;
        }

        long date = data.getLong(COL_WEATHER_DATE);
        String friendlyDateText = Utility.getDayName(getActivity(), date);
        String dateString = Utility.getFormattedMonthDay(getActivity(), date);
        String weatherDescription = data.getString(COL_WEATHER_DESC);
        String maxTemperature = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP));
        String minTemperature = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP));
        String humidity = getActivity().getResources().getString(R.string.format_humidity,data.getFloat(COL_WEATHER_HUMDITY));
        String wind = Utility.getFormattedWind(getActivity(), data.getFloat(COL_WEATHER_WIND_SPEED), data.getFloat(COL_WEATHER_DEGRESS));
        String pressure = getActivity().getResources().getString(R.string.format_pressure,data.getFloat(COL_WEATHER_PRESSURE));
        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);

        tvDay.setText(friendlyDateText);
        tvDate.setText(dateString);
        tvForecast.setText(weatherDescription);
        tvHight.setText(maxTemperature);
        tvLow.setText(minTemperature);
        tvHumidity.setText(humidity);
        tvWind.setText(wind);
        tvPressure.setText(pressure);
        imageWeather.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        imageWeather.setContentDescription(weatherDescription);

        mForecast = String.format("%s - %s - %s/%s",dateString,weatherDescription,maxTemperature,minTemperature);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID_DETAILS,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onLocationChanged(String newLocation){
        Uri uri = mUri;
        if(null != uri){
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updateUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation,date);
            mUri = updateUri;
            getLoaderManager().restartLoader(LOADER_ID_DETAILS, null,this);
        }
    }

    public void onDefaultChanged(String location, long date) {
        Uri uri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(location,date);
        Cursor cursor = getActivity().getContentResolver().query(uri,FORECAST_COLUMNS,null,null,null);
        loadData(cursor);
    }
}
