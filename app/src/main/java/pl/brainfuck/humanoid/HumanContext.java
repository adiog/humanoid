// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid;

import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pl.brainfuck.humanoid.canvas.core.Canvas;
import pl.brainfuck.humanoid.canvas.model.Segment;
import pl.brainfuck.humanoid.engine.Engine;
import pl.brainfuck.humanoid.sensor.FusionData;
import pl.brainfuck.humanoid.sensor.Kalman;
import pl.brainfuck.humanoid.sensor.SensorData;


public class HumanContext {


    public float yaw = 0;
    public float pitch = 0;
    public float previousX = 0;
    public float previousY = 0;
    public Config config = new Config();
    public Canvas canvas = new Canvas();
    public Engine engine = new Engine(this);

    public Sensor accelerometer;
    public Lock lock = new ReentrantLock(true);

    private AppCompatActivity activity;

    public HumanContext(AppCompatActivity activity) {
        this.activity = activity;

        fusionDataMap.put(Segment.FootLeft, new FusionData(0, 90));
        fusionDataMap.put(Segment.FootRight, new FusionData(0, 90));
    }

    public void log(String text) {
        activity.setTitle(text);
    }

    private Map<Segment, Kalman> kalmanMap = new HashMap<>();
    private Map<Segment, FusionData> fusionDataMap = new HashMap<>();

    public void update(Segment segment, SensorData sensorData) {
        Kalman kalman = this.kalmanMap.get(segment);
        if (kalman == null) {
            kalman = new Kalman();
            kalman.initialize(sensorData);
            this.kalmanMap.put(segment, kalman);
        }
        FusionData fusionData = kalman.apply(sensorData);

        try {
            lock.lock();
            fusionDataMap.put(segment, fusionData);
        } catch (Exception e) {
            // handle the exception
        } finally {
            lock.unlock();
        }
    }

    public FusionData get(Segment segment) {
        try {
            lock.lock();
            return fusionDataMap.get(segment);
        } catch (Exception e) {
            // handle the exception
        } finally {
            lock.unlock();
        }
        return null;
    }


}
