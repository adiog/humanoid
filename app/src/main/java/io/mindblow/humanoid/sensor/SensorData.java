// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.sensor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class SensorData {
    public float[] accelerometer = new float[3];
    public float[] gyroscope = new float[3];

    public SensorData() {

    }

    public SensorData(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        accelerometer[0] = byteBuffer.getFloat();
        accelerometer[1] = byteBuffer.getFloat();
        accelerometer[2] = byteBuffer.getFloat();
        gyroscope[0] = byteBuffer.getFloat();
        gyroscope[1] = byteBuffer.getFloat();
        gyroscope[2] = byteBuffer.getFloat();
    }

    public static byte[] store(float[] accelerometer, float[] gyroscope) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(24);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putFloat(accelerometer[0]);
        byteBuffer.putFloat(accelerometer[1]);
        byteBuffer.putFloat(accelerometer[2]);
        byteBuffer.putFloat(gyroscope[0]);
        byteBuffer.putFloat(gyroscope[1]);
        byteBuffer.putFloat(gyroscope[2]);
        return byteBuffer.array();
    }

    public byte[] store() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(24);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putFloat(accelerometer[0]);
        byteBuffer.putFloat(accelerometer[1]);
        byteBuffer.putFloat(accelerometer[2]);
        byteBuffer.putFloat(gyroscope[0]);
        byteBuffer.putFloat(gyroscope[1]);
        byteBuffer.putFloat(gyroscope[2]);
        return byteBuffer.array();
    }
}
