package com.example.androidclass_hikerswatch_exercise;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude;
    TextView longitude;
    TextView accuracy;
    TextView altitude;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = findViewById(R.id.latitude_textView);
        longitude = findViewById(R.id.longitude_textView);
        accuracy = findViewById(R.id.accuracy_textView);
        altitude = findViewById(R.id.altitude_textView);
        address = findViewById(R.id.address_textView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , 1);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , 0,
                    0,
                    locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                updateLocation(lastKnownLocation);
            }
        }
    }

    private void updateLocation(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addressList != null && addressList.size() > 0) {

                Log.i("Address:", address.toString());
                String addressString = "Address:\n";

                if (addressList.get(0).getThoroughfare() != null) {
                    addressString += addressList.get(0).getThoroughfare() + "\n";
                }
                if (addressList.get(0).getLocality() != null) {
                    addressString += addressList.get(0).getLocality() + " ";
                }
                if (addressList.get(0).getAdminArea() != null) {
                    addressString += addressList.get(0).getAdminArea() + " ";
                }
                if (addressList.get(0).getPostalCode() != null) {
                    addressString += addressList.get(0).getPostalCode();
                }
                address.setText(addressString);

                if (addressList.get(0).hasLatitude()) {
                    latitude.setText("Latitude: " + addressList.get(0).getLatitude());
                }
                if (addressList.get(0).hasLongitude()) {
                    longitude.setText("Longitude: " + addressList.get(0).getLongitude());
                }
                accuracy.setText("Accuracy: " + Double.toString(location.getAccuracy()));
                altitude.setText("Altitude: " + Double.toString(location.getAltitude()));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}
