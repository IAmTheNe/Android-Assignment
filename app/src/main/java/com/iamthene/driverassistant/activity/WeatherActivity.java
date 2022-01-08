package com.iamthene.driverassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.WeatherData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WeatherActivity extends AppCompatActivity {
    final String api_key = "483055cf079833580aa3b0fb643b869e";
    final String weather_url = "https://api.openweathermap.org/data/2.5/weather";

    final long min_time = 5000;
    final float min_distance = 1000;
    final int REQUEST_CODE = 101;

    String location_provider = LocationManager.GPS_PROVIDER;

    TextView tvCityName, tvWeatherCondition, tvTemperature;
    ImageView imgWeatherIcon;
    LinearLayout cityFinder;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        inIt();
    }

    public void inIt() {
        tvCityName = findViewById(R.id.tvCityName);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeatherCondition = findViewById(R.id.tvWeatherCondition);
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon);
        cityFinder = findViewById(R.id.cityFinder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longtitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longtitude);
                params.put("appid", api_key);
                letsdoSomeNetworking(params);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(location_provider, min_time, min_distance, locationListener);
    }

    private void letsdoSomeNetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(weather_url, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                Toast.makeText(WeatherActivity.this, "Data get success", Toast.LENGTH_SHORT).show();

                WeatherData weatherData = WeatherData.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void updateUI(WeatherData weatherData) {
        tvTemperature.setText(weatherData.getmTemperature());
        tvCityName.setText(weatherData.getmCity());
        tvWeatherCondition.setText(weatherData.getmWeatherType());

        int resourceId = getResources().getIdentifier(weatherData.getmIcon(), "drawable", getPackageName());
        imgWeatherIcon.setImageResource(resourceId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(WeatherActivity.this, "Location get successful", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else {

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}