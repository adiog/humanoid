// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.context;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import io.mindblow.humanoid.bluetooth.BluetoothHumanoidService;
import io.mindblow.humanoid.sensor.SensorData;

public class SlaveContext extends Thread {
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothHumanoidService bluetoothHumanoidService = null;

    public Sensor accelerometerSensor;
    public Sensor gyroscopeSensor;

    public SensorData sensorData = new SensorData();

    public SlaveContext(SensorManager sensorManager, String bluetoothAddress) {
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bluetoothAddress);
        bluetoothHumanoidService = new BluetoothHumanoidService(null);
        bluetoothHumanoidService.connect(device, false);
    }

    public synchronized void updateAccelerometer(float x, float y, float z) {
        sensorData.accelerometer[0] = x;
        sensorData.accelerometer[1] = -z;
        sensorData.accelerometer[2] = -y;
    }

    public synchronized void updateGyroscope(float x, float y, float z) {
        sensorData.gyroscope[0] = x;
        sensorData.gyroscope[1] = -z;
        sensorData.gyroscope[2] = -y;
    }

    private synchronized byte[] serialize() {
        return sensorData.store();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            bluetoothHumanoidService.write(serialize());
        }
    }
}
