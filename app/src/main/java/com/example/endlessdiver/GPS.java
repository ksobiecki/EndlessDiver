package com.example.endlessdiver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPS implements LocationListener {

    Context context;
    Geocoder geocoder;
    List<Address> addresses;

    public GPS(Context c) {
        context = c;
        geocoder = new Geocoder(c, Locale.getDefault());
    }

    public Location getLocation() {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show();
            return null;
        }

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGPSEnabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        } else {
            Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public String getLocalization() {

        Location location = getLocation();

        try {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);

                return address;
            }
            else return "wywalenie 1";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "wywalenie 2";
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
