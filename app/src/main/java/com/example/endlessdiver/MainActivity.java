package com.example.endlessdiver;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // to be changed to get latitude and longitude from GPS
    Geocoder geocoder;
    List<Address> addresses;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("" + prefs.getInt("highscore", 0));

        geocoder = new Geocoder(this, Locale.getDefault());
        TextView addressTxt = findViewById(R.id.address);
        addressTxt.setText(getLocalization() + "");

    }

    private String getLocalization() {
        GPS gps = new GPS(getApplicationContext());
        Location location = gps.getLocation();

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
}
