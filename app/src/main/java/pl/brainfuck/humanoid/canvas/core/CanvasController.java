// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.canvas.core;

import pl.brainfuck.humanoid.HumanContext;

public class CanvasController extends Thread {
    private HumanContext humanContext;

    public CanvasController(HumanContext humanContext) {
        this.humanContext = humanContext;
    }

    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(humanContext.config.canvasIntervalMillis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            humanContext.canvas.tick();
        }
    }
}
