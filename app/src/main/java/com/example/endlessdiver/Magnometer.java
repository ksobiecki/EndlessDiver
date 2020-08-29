package com.example.endlessdiver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Magnometer implements SensorEventListener {

    private SensorManager sensorManager;
    private float magnometerValue;

    Magnometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public float getValue()
    {
        return magnometerValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float magX = event.values[0];
        float magY = event.values[1];
        float magZ = event.values[2];

        double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
        magnometerValue = (float) magnitude;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}

