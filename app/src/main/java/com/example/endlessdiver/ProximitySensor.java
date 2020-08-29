package com.example.endlessdiver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ProximitySensor implements SensorEventListener {

    private SensorManager sensorManager;
    private float proximitySensorValue;

    ProximitySensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public float getValue()
    {
        return proximitySensorValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        proximitySensorValue = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}