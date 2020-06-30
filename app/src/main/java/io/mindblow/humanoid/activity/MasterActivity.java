// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import io.mindblow.humanoid.context.MasterContext;
import io.mindblow.humanoid.R;
import io.mindblow.humanoid.canvas.core.CanvasController;
import io.mindblow.humanoid.engine.EngineController;

public class MasterActivity extends AppCompatActivity {
    private MasterContext masterContext;
    private CanvasController canvasController;
    private EngineController engineController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        masterContext = new MasterContext(this);
        canvasController = new CanvasController(masterContext);
        engineController = new EngineController(masterContext);
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        d.getMetrics(masterContext.config.displayMetrics);

        masterContext.canvas.onCreate(this, masterContext);
        canvasController.start();
        engineController.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        masterContext.canvas.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        masterContext.canvas.onResume();
    }
}
