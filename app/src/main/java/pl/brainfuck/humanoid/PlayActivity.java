// This file is a part of Humanoid project.
// Copyright (C) 2019 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import java.net.SocketException;

import pl.brainfuck.humanoid.canvas.core.CanvasController;
import pl.brainfuck.humanoid.engine.EngineController;
import pl.brainfuck.humanoid.network.UdpServer;


public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private HumanContext humanContext = new HumanContext(this);
    private CanvasController canvasController = new CanvasController(humanContext);
    private EngineController engineController = new EngineController(humanContext);

    //private SensorManager sensorManager;

    private UdpServer udpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        try {
            udpServer = new UdpServer(this.humanContext);
            udpServer.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        d.getMetrics(humanContext.config.displayMetrics);

        humanContext.canvas.onCreate(this, humanContext);
        canvasController.start();
        engineController.start();

        //sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
        //    humanContext.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //    sensorManager.registerListener(this, humanContext.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //}
    }

    @Override
    protected void onPause() {
        super.onPause();
        humanContext.canvas.onPause();
        //sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();humanContext.canvas.onResume();
        //sensorManager.registerListener(this, humanContext.accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //humanContext.log(String.valueOf(-event.values[0]) + " x " + String.valueOf(-event.values[1]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
