// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.canvas.core;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;

import io.mindblow.humanoid.context.MasterContext;


public class Canvas {
    private GLSurfaceView glSurfaceView;
    private AppCompatActivity activity;

    public void onCreate(AppCompatActivity activity_, MasterContext masterContext) {
        activity = activity_;
        glSurfaceView = new SurfaceView(activity, masterContext);
        activity.setContentView(glSurfaceView);
    }

    public void onPause() {
        glSurfaceView.onPause();
    }

    public void onResume() {
        glSurfaceView.onResume();
    }

    public void tick() {
        glSurfaceView.requestRender();
    }
}
