package edu.uw.tcss450.groupchat.model.weather;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.tcss450.groupchat.R;

/**
 * A class that get current weather information.
 *
 * @version December 8, 2020
 */
public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    private boolean mInitialized = false;

    /**
     * A constructor
     *
     * @param application the reference to the current application.
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>(new JSONObject());
    }

    /**
     * Add observer for receiving server's responses.
     *
     * @param owner The LifeCycle owner that will control the observer
     * @param observer The observer that will receive the events
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    public void initialize() {
        mInitialized = true;
    }

    public void connect(final double lat, final double lon) {
        String url = getApplication().getResources().getString(R.string.base_url)
                + "weather?lat=" + lat + "&lon=" + lon;

        Request request = new JsonObjectRequest(

                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Perform an HTTP request to retrieve current weather information.
     *
     * @param zip current location zip code
     */
    public void connectZip(final String zip) {
        String url = getApplication().getResources().getString(R.string.base_url)
                + "weather?zip=" + zip;

        Request request = new JsonObjectRequest(

                Request.Method.GET,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}
