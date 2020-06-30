// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.canvas.core;

import io.mindblow.humanoid.context.MasterContext;

public class CanvasController extends Thread {
    private MasterContext masterContext;

    public CanvasController(MasterContext masterContext) {
        this.masterContext = masterContext;
    }

    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(masterContext.config.canvasIntervalMillis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            masterContext.canvas.tick();
        }
    }
}
