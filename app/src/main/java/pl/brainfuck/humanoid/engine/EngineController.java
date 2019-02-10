// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.engine;

import pl.brainfuck.humanoid.HumanContext;


public class EngineController extends Thread {
    private HumanContext humanContext;

    public EngineController(HumanContext humanContext) {
        this.humanContext = humanContext;
    }

    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(humanContext.config.engineIntervalMillis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            humanContext.engine.tick(humanContext.config.engineIntervalSeconds);
        }
    }
}
