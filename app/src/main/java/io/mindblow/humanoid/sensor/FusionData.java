// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.sensor;


public class FusionData {
    public float roll;
    public float pitch;

    public FusionData(float roll, float pitch) {
        this.roll = roll;
        this.pitch = pitch;
    }
}
