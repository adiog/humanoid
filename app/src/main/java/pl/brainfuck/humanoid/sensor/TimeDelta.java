// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.sensor;


class TimeDelta {
    private long previousMillis = System.currentTimeMillis();

    float get()
    {
        long currentMillis = System.currentTimeMillis();
        float returnSeconds = (float)(currentMillis - previousMillis) / 1000.0F;
        previousMillis = currentMillis;
        return returnSeconds;
    }
}
