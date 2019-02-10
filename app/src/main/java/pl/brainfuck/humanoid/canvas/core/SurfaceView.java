// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.canvas.core;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import pl.brainfuck.humanoid.HumanContext;

public class SurfaceView extends GLSurfaceView {

    private final pl.brainfuck.humanoid.canvas.core.Renderer renderer;
    private final HumanContext humanContext;

    public SurfaceView(Context context, HumanContext humanContext) {
        super(context);

        this.humanContext = humanContext;

        setEGLContextClientVersion(2);

        renderer = new pl.brainfuck.humanoid.canvas.core.Renderer(humanContext);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return humanContext.engine.registerEvent(e);
    }
}
