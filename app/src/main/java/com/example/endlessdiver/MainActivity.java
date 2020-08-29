package com.example.endlessdiver;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

        final GPS gps = new GPS (getApplicationContext());
        final TextView addressTxt = findViewById(R.id.address);
        addressTxt.setText(gps.getLocalization()+"");

        findViewById(R.id.addressButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressTxt.setText(gps.getLocalization()+"");
            }
        });

    }

}
