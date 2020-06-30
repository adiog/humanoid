// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.mindblow.humanoid.R;
import io.mindblow.humanoid.context.SlaveContext;
import io.mindblow.humanoid.sensor.FusionData;
import io.mindblow.humanoid.sensor.Kalman;

public class SlaveActivity extends AppCompatActivity implements SensorEventListener {
    private SlaveContext slaveContext;

    private SensorManager sensorManager;

    Kalman kalman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        ((Button) findViewById(R.id.buttonBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String bluetoothAddress = extras.getString(SelectActivity.EXTRA_DEVICE_ADDRESS);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        slaveContext = new SlaveContext(sensorManager, bluetoothAddress);
        slaveContext.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, slaveContext.accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, slaveContext.gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private final int[] aids = new int[]{
            R.id.buttonAX,
            R.id.buttonAY,
            R.id.buttonAZ
    };

    private final int[] gids = new int[]{
            R.id.buttonGX,
            R.id.buttonGY,
            R.id.buttonGZ
    };

    private final String[] axis = new String[]{"X", "Y", "Z"};

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == slaveContext.accelerometerSensor) {
            slaveContext.updateAccelerometer(event.values[0], event.values[1], event.values[2]);
            for (int i = 0; i < 3; i++) {
                ((Button) findViewById(aids[i])).setText(String.format("A%s: %f", axis[i], slaveContext.sensorData.accelerometer[i]));
            }
        } else if (event.sensor == slaveContext.gyroscopeSensor) {
            slaveContext.updateGyroscope(event.values[0], event.values[1], event.values[2]);
            for (int i = 0; i < 3; i++) {
                ((Button) findViewById(gids[i])).setText(String.format("G%s: %f", axis[i], slaveContext.sensorData.gyroscope[i]));
            }
        }

        if (kalman == null) {
            kalman = new Kalman();
            kalman.initialize(slaveContext.sensorData);
        }
        FusionData fusionData = kalman.apply(slaveContext.sensorData);
        ((Button) findViewById(R.id.buttonPitchRaw)).setText(String.format("Raw Pitch: %f", Kalman.getPitch(slaveContext.sensorData.accelerometer)));
        ((Button) findViewById(R.id.buttonRollRaw)).setText(String.format("Raw Roll: %f", Kalman.getRoll(slaveContext.sensorData.accelerometer)));
        ((Button) findViewById(R.id.buttonPitch)).setText(String.format("Kalman Pitch: %f", fusionData.pitch));
        ((Button) findViewById(R.id.buttonRoll)).setText(String.format("Kalman Roll: %f", fusionData.roll));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
