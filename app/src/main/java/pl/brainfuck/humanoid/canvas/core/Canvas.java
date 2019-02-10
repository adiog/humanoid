// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.canvas.core;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;

import pl.brainfuck.humanoid.HumanContext;


public class Canvas {
    private GLSurfaceView glSurfaceView;
    private AppCompatActivity activity;

    public void onCreate(AppCompatActivity activity_, HumanContext humanContext) {
        activity = activity_;
        glSurfaceView = new SurfaceView(activity, humanContext);
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
