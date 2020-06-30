// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.engine;

import android.view.MotionEvent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.mindblow.humanoid.context.MasterContext;


public class Engine {
    public MasterContext masterContext;
    private final Lock lock = new ReentrantLock(true);

    public Engine(MasterContext masterContext) {
        this.masterContext = masterContext;
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
                masterContext.yaw += -(masterContext.previousY - y) / 3;
                masterContext.yaw %= 360;
                masterContext.pitch -= (masterContext.previousX - x) / 3;
                masterContext.pitch %= 360;
                break;
            case MotionEvent.ACTION_UP:
                // event end
                break;
            case MotionEvent.ACTION_CANCEL:
                // event cancel
                break;
        }

        masterContext.previousX = x;
        masterContext.previousY = y;

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
