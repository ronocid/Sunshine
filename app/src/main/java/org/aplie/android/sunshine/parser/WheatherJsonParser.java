package org.aplie.android.sunshine.parser;


import org.json.JSONException;
import org.json.JSONObject;

public class WheatherJsonParser {
    final String OWM_CITY = "city";
    final String OWM_CITY_NAME = "name";
    final String OWM_COORD = "coord";

    final String OWM_LATITUDE = "lat";
    final String OWM_LONGITUDE = "lon";

    final String OWM_LIST = "list";

    final String OWM_PRESSURE = "pressure";
    final String OWM_HUMIDITY = "humidity";
    final String OWM_WINDSPEED = "speed";
    final String OWM_WIND_DIRECTION = "deg";

    final String OWM_TEMPERATURE = "temp";
    final String OWM_MAX = "max";
    final String OWM_MIN = "min";

    final String OWM_WEATHER = "weather";
    final String OWM_DESCRIPTION = "main";
    final String OWM_WEATHER_ID = "id";

    public JSONObject getWeatherDataFromJson(String forecastJsonStr)throws JSONException {
        return null;
    }
}
