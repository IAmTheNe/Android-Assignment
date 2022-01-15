package com.iamthene.driverassistant.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.iamthene.driverassistant.R;

import java.io.IOException;
import java.util.List;

public class FindGasStationActivity extends AppCompatActivity {
    EditText etAddress;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_gas_station);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.my_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(FindGasStationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(FindGasStationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        etAddress = findViewById(R.id.etAddress);
        etAddress.setOnClickListener(view -> {
            Geocoder geocoder = new Geocoder(FindGasStationActivity.this);
            try {
                List<Address> lstAddress = geocoder.getFromLocationName(etAddress.getText().toString(), 1);
                double lat = lstAddress.get(0).getLatitude();
                double lon = lstAddress.get(0).getLongitude();
                String address = lstAddress.get(0).getAddressLine(0);
                LatLng latLng = new LatLng(lat, lon);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address).snippet(lstAddress.get(0).getLocality());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                map.addMarker(markerOptions);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            supportMapFragment.getMapAsync(googleMap -> {
                map = googleMap;
                Geocoder geocoder = new Geocoder(this);
                List<Address> lstAddress;
                try {
                    lstAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    double lat = lstAddress.get(0).getLatitude();
                    double lon = lstAddress.get(0).getLongitude();
                    String address = lstAddress.get(0).getAddressLine(0);
                    LatLng latLng = new LatLng(lat, lon);
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address).snippet("My Location");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    googleMap.addMarker(markerOptions);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

}