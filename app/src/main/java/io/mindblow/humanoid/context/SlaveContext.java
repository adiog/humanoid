// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.context;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import io.mindblow.humanoid.bluetooth.BluetoothHumanoidService;
import io.mindblow.humanoid.sensor.SensorData;

public class SlaveContext extends Thread {
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothHumanoidService bluetoothHumanoidService = null;

    float[] calibrationMatrix = new float[16];

    public Sensor accelerometerSensor;
    public Sensor gyroscopeSensor;

    public SensorData sensorData = new SensorData();

    public SlaveContext(SensorManager sensorManager, String bluetoothAddress) {
        Matrix.setIdentityM(calibrationMatrix, 0);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bluetoothAddress);
        bluetoothHumanoidService = new BluetoothHumanoidService(null);
        bluetoothHumanoidService.connect(device, false);
    }

    public synchronized void updateAccelerometer(float x, float y, float z) {
        float[] input = new float[]{x, -z, -y, 1};
        Matrix.multiplyMV(sensorData.accelerometer, 0, calibrationMatrix, 0, input, 0);
    }

    public synchronized void updateGyroscope(float x, float y, float z) {
        float[] input = new float[]{x, -z, -y, 1};
        Matrix.multiplyMV(sensorData.gyroscope, 0, calibrationMatrix, 0, input, 0);
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

    public void reset() {
        Matrix.setIdentityM(calibrationMatrix, 0);
    }

    public void rotate90(int x, int y, int z) {
        float[] rotationMatrix = new float[16];
        float[] tempMatrix = calibrationMatrix.clone();
        Matrix.setRotateM(rotationMatrix, 0, 90, x, y, z);
        Matrix.multiplyMM(calibrationMatrix, 0, tempMatrix, 0, rotationMatrix, 0);
    }
}
