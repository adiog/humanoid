// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.engine;

import io.mindblow.humanoid.context.MasterContext;


public class EngineController extends Thread {
    private MasterContext masterContext;

    public EngineController(MasterContext masterContext) {
        this.masterContext = masterContext;
    }

    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(masterContext.config.engineIntervalMillis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            masterContext.engine.tick(masterContext.config.engineIntervalSeconds);
        }
    }
}
