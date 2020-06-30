// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.sensor;


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
