// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.engine;

import android.view.MotionEvent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pl.brainfuck.humanoid.HumanContext;


public class Engine {
    public HumanContext humanContext;
    private final Lock lock = new ReentrantLock(true);

    public Engine(HumanContext humanContext) {
        this.humanContext = humanContext;
    }

    public boolean registerEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // event start
                break;
            case MotionEvent.ACTION_MOVE:
                // event update
                humanContext.yaw += -(humanContext.previousY - y) / 3;
                humanContext.yaw %= 360;
                humanContext.pitch += (humanContext.previousX - x) / 3;
                humanContext.pitch %= 360;
                break;
            case MotionEvent.ACTION_UP:
                // event end
                break;
            case MotionEvent.ACTION_CANCEL:
                // event cancel
                break;
        }

        humanContext.previousX = x;
        humanContext.previousY = y;

        return true;
    }

    private void syncronizedTick(float timeDelta) {
    }

    void tick(float timeDelta) {
        try {
            lock.lock();
            syncronizedTick(timeDelta);
        } catch (Exception e) {
            // handle the exception
        } finally {
            lock.unlock();
        }
    }
}
