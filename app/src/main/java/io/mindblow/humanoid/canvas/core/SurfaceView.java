// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.canvas.core;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import io.mindblow.humanoid.context.MasterContext;

public class SurfaceView extends GLSurfaceView {

    private final io.mindblow.humanoid.canvas.core.Renderer renderer;
    private final MasterContext masterContext;

    public SurfaceView(Context context, MasterContext masterContext) {
        super(context);

        this.masterContext = masterContext;

        setEGLContextClientVersion(2);

        renderer = new io.mindblow.humanoid.canvas.core.Renderer(masterContext);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return masterContext.engine.registerEvent(e);
    }
}
