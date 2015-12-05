package org.aplie.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastAdapter extends CursorAdapter{

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageWeather = (ImageView) view.findViewById(R.id.list_item_icon);
        TextView tvDate = (TextView) view.findViewById(R.id.list_item_date_textview);
        TextView tvForecast = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        TextView tvHight = (TextView) view.findViewById(R.id.list_item_high_textview);
        TextView tvLow = (TextView) view.findViewById(R.id.list_item_low_textview);


        boolean isMetric = Utility.isMetric(context);
        Long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        Double max = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        Double min = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

        tvDate.setText(Utility.getFriendlyDayString(context,date));
        tvForecast.setText(description);
        tvHight.setText(Utility.formatTemperature(max,isMetric)+"º");
        tvLow.setText(Utility.formatTemperature(min,isMetric)+"º");
    }
}
