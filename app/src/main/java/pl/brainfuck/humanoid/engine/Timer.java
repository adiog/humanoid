// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.engine;


public class Timer {
    private long startingPointMillis;
    private long expirePointMillis;

    public Timer()
    {
    }

    public Timer(long timeoutMillis)
    {
        reset(timeoutMillis);
    }

    public boolean isElapsed()
    {
        long currentMillis = System.currentTimeMillis();
        return (expirePointMillis <= currentMillis);
    }

    void reset(long timeoutMillis)
    {
        startingPointMillis = System.currentTimeMillis();
        expirePointMillis = startingPointMillis + timeoutMillis;
    }

    public float getRatio()
    {
        long currentMillis = System.currentTimeMillis();

        if (currentMillis <= startingPointMillis) {
            return 1.0f;
        }

        if (expirePointMillis <= currentMillis) {
            return 0.0f;
        }

        return (float) (expirePointMillis - currentMillis) / (float) (expirePointMillis - startingPointMillis);
    }
}
