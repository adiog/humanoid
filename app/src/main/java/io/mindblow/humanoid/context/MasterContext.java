// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.context;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.mindblow.humanoid.Config;
import io.mindblow.humanoid.bluetooth.BluetoothHumanoidService;
import io.mindblow.humanoid.bluetooth.ClientMatcher;
import io.mindblow.humanoid.canvas.core.Canvas;
import io.mindblow.humanoid.canvas.model.Segment;
import io.mindblow.humanoid.engine.Engine;
import io.mindblow.humanoid.sensor.FusionData;
import io.mindblow.humanoid.sensor.Kalman;
import io.mindblow.humanoid.sensor.SensorData;


public class MasterContext {
    public float yaw = 0;
    public float pitch = 0;
    public float previousX = 0;
    public float previousY = 0;
    public Config config = new Config();
    public Canvas canvas = new Canvas();
    public Engine engine = new Engine(this);

    private ClientMatcher clientMatcher;

    private BluetoothHumanoidService bluetoothHumanoidService = null;

    public Lock lock = new ReentrantLock(true);

    public MasterContext(Context context) {
        clientMatcher = new ClientMatcher(context);
        fusionDataMap.put(Segment.FootLeft, new FusionData(0, 90));
        fusionDataMap.put(Segment.FootRight, new FusionData(0, 90));

        bluetoothHumanoidService = new BluetoothHumanoidService(this);
        bluetoothHumanoidService.listen();
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

//        Log.i("KALMAN", String.format("Pitch %f Roll %f", fusionData.pitch, fusionData.roll));

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

    public void onData(String sender, byte[] data, int size) {
        if (size != 24) {
            return;
        }
        Segment segment = clientMatcher.match(sender);
        if (segment == null) {
            return;
        }
        SensorData sensorData = new SensorData(data);
        update(segment, sensorData);
    }
}
