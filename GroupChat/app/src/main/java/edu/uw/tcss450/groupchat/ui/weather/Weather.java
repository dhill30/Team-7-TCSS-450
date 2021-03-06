package edu.uw.tcss450.groupchat.ui.weather;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import edu.uw.tcss450.groupchat.R;

/**
 * This class stores relevant weather information for a given location at a given time.
 *
 * @version December 31, 2020
 */
public class Weather {

    private final String mType;
    private final Date mTime;
    private final String mTimezone;
    private final String mMain;
    private final int mIcon;
    private final double mTemp;
    private final String mHumidity;
    private final double mWindSpeed;
    private double mTempLow;
    private double mTempHigh;

    /**
     * Constructor for this class.
     * @param type the weather type
     * @param time the date/time in UTC
     * @param timezone the timezone of the location
     * @param main short description of the weather
     * @param icon the icon identification string
     * @param temp the weather temperature
     * @param humidity the weather humidity percentage
     * @param windSpeed the weather wind speed
     */
    public Weather(String type, long time, String timezone, String main,
                   String icon, double temp, int humidity, double windSpeed) {
        mType = type;
        mTime = new Date(time * 1000);
        mTimezone = timezone;
        mMain = main;
        mIcon = getIconId(icon);
        mTemp = temp;
        mHumidity = String.valueOf(humidity);
        mWindSpeed = windSpeed;
    }

    /**
     * Returns the time for this weather (forecast). The format is dictated by the type.
     * @return the formatted time in location-local timezone
     */
    public String getTime() {
        SimpleDateFormat sdf;
        switch (mType) {
            case "hourly":
                sdf = new SimpleDateFormat("ha", Locale.getDefault());
                break;
            case "daily":
                sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                break;
            default:
                sdf = new SimpleDateFormat("EEEE, h:mm a", Locale.getDefault());
                break;
        }
        sdf.setTimeZone(TimeZone.getTimeZone(mTimezone));
        return sdf.format(mTime).toUpperCase();
    }

    /**
     * Returns the short weather description for this forecast.
     * @return short weather description
     */
    public String getMain() {
        return mMain;
    }

    /**
     * Returns the condition icon drawable id.
     * @return condition icon id
     */
    public int getIcon() {
        return mIcon;
    }

    /**
     * Returns the current temperature in fahrenheit or celsius
     * @param metric used to determine whether to display in imperial or metric
     * @return current temperature
     */
    public String getTemp(final boolean metric) {
        if (!mType.equals("daily")) {
            if (!metric) {
                return String.valueOf((int) mTemp);
            } else {
                return String.valueOf((int) ((mTemp - 32.0) * 5 / 9));
            }
        } else {
            if (!metric) {
                return (int) mTempLow + "° / " + (int) mTempHigh + "°";
            } else {
                int low = (int) (mTempLow - 32.0) * 5 / 9;
                int high = (int) (mTempHigh - 32.0) * 5 / 9;
                return low + "° / " + high + "°";
            }
        }
    }

    /**
     * Returns the humidity of this weather
     * @return humidity of forecast
     */
    public String getHumidity() {
        return mHumidity;
    }

    /**
     * Returns the wind speed of this weather
     * @param metric used to determine whether to display in imperial or metric
     * @return weather wind speed
     */
    public String getWindSpeed(final boolean metric) {
        DecimalFormat df = new DecimalFormat("#0.0");
        if (metric) {
            return df.format((mWindSpeed - 32.0) * 5 / 9);
        } else {
            return df.format(mWindSpeed);
        }
    }

    /**
     * Sets this weather's low and high temps for the day
     * @param low lowest temp for the day
     * @param high highest temp for the day
     */
    public void setTemp(double low, double high) {
        mTempLow = low;
        mTempHigh = high;
    }

    private int getIconId(final String icon) {
        switch (icon) {
            case "01d":
                return R.drawable.ic_weather_01d;
            case "01n":
                return R.drawable.ic_weather_01n;
            case "02d":
                return R.drawable.ic_weather_02d;
            case "02n":
                return R.drawable.ic_weather_02n;
            case "03d":
            case "03n":
                return R.drawable.ic_weather_03dn;
            case "04d":
            case "04n":
                return R.drawable.ic_weather_04dn;
            case "09d":
            case "09n":
                return R.drawable.ic_weather_09dn;
            case "10d":
                return R.drawable.ic_weather_10d;
            case "10n":
                return R.drawable.ic_weather_10n;
            case "11d":
            case "11n":
                return R.drawable.ic_weather_11dn;
            case "13d":
            case "13n":
                return R.drawable.ic_weather_13dn;
            case "50d":
            case "50n":
                return R.drawable.ic_weather_50dn;
            default:
                return -1;
        }
    }
}
