// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.sensor;


public class Kalman {

    private static float degrees(float radians) {
        return radians * 180.0F / ((float) Math.PI);
    }

    public static Float getRoll(float[] accelerometer) {
        float v = degrees((float) Math.atan2(accelerometer[0], accelerometer[2]));
        if (v > 0) {
            return 180 - v;
        } else {
            return -(180 + v);
        }
    }

    public static Float getPitch(float[] accelerometer) {
        return degrees((float) Math.atan(-accelerometer[1] / Math.sqrt(accelerometer[0] * accelerometer[0] + accelerometer[2] * accelerometer[2])));
    }

    public void initialize(SensorData sensorData) {
        float roll = getRoll(sensorData.accelerometer);
        float pitch = getPitch(sensorData.accelerometer);

        kalmanX.setAngle(roll);
        kalmanY.setAngle(pitch);

        kalAngleX = roll;
        kalAngleY = pitch;
    }

    private float convertToDegreePerSecond(float unit) {
        return unit / 131.0F;
    }

    public FusionData apply(SensorData sensorData) {
        float dt = timeDelta.get();

        float roll = getRoll(sensorData.accelerometer);
        float pitch = getPitch(sensorData.accelerometer);

        float gyroXrate = convertToDegreePerSecond(sensorData.gyroscope[0]);
        float gyroYrate = convertToDegreePerSecond(sensorData.gyroscope[1]);

        if ((roll < -90 && kalAngleX > 90) || (roll > 90 && kalAngleX < -90)) {
            kalmanX.setAngle(roll);
            kalAngleX = roll;
        } else {
            kalAngleX = kalmanX.getAngle(roll, gyroXrate, dt);
        }

        if (Math.abs(kalAngleX) > 90) {
            gyroYrate = -gyroYrate;
        }
        kalAngleY = kalmanY.getAngle(pitch, gyroYrate, dt);

        return new FusionData(kalAngleX, kalAngleY);
    }

    private KalmanInternal kalmanX = new KalmanInternal();
    private KalmanInternal kalmanY = new KalmanInternal();

    private float kalAngleX;
    private float kalAngleY;

    private TimeDelta timeDelta = new TimeDelta();
}
