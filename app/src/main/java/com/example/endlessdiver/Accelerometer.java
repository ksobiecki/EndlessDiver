package com.example.endlessdiver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer implements SensorEventListener {

    private SensorManager sensorManager;
    private float accelerometerValue;

    Accelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public float getValue()
    {
        return accelerometerValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float accX = event.values[0];

        accelerometerValue = accX;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}
