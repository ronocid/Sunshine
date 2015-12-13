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
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;
    private boolean mUseTodayLayout = true;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUseTodayLayout (boolean useTodayLayout){
        mUseTodayLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if(viewType == VIEW_TYPE_TODAY){
            layoutId = R.layout.list_item_forecast_today;
        }else if (viewType == VIEW_TYPE_FUTURE_DAY){
            layoutId = R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(getItemViewType(cursor.getPosition()) == VIEW_TYPE_TODAY){
            viewHolder.imageWeather.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        }else if (getItemViewType(cursor.getPosition()) == VIEW_TYPE_FUTURE_DAY){
            viewHolder.imageWeather.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        }
        boolean isMetric = Utility.isMetric(context);
        Long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        Double max = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        Double min = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

        viewHolder.imageWeather.setContentDescription(description);
        viewHolder.tvDate.setText(Utility.getFriendlyDayString(context,date));
        viewHolder.tvForecast.setText(description);
        viewHolder.tvHight.setText(Utility.formatTemperature(context, max));
        viewHolder.tvLow.setText(Utility.formatTemperature(context,min));
    }

    public static class ViewHolder{
        public final ImageView imageWeather;
        public final TextView tvDate;
        public final TextView tvForecast;
        public final TextView tvHight;
        public final TextView tvLow;

        public ViewHolder(View view){
            imageWeather = (ImageView) view.findViewById(R.id.list_item_icon);
            tvDate = (TextView) view.findViewById(R.id.list_item_date_textview);
            tvForecast = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            tvHight = (TextView) view.findViewById(R.id.list_item_high_textview);
            tvLow = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
}
