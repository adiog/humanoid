// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.sensor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class SensorData {
    float[] accelerometer = new float[3];
    float[] gyroscope = new float[3];
    float[] magnetometer = new float[3];

    public void parse(byte[] buffer)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        accelerometer[1] = byteBuffer.getFloat();
        accelerometer[2] = byteBuffer.getFloat();
        accelerometer[0] = byteBuffer.getFloat();
        gyroscope[1] = byteBuffer.getFloat();
        gyroscope[2] = byteBuffer.getFloat();
        gyroscope[0] = byteBuffer.getFloat();
        magnetometer[1] = byteBuffer.getFloat();
        magnetometer[2] = byteBuffer.getFloat();
        magnetometer[0] = byteBuffer.getFloat();
    }
}
