// This file is a part of Humanoid project.
// Copyright (C) 2019 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.sensor;


public class FusionData {
    public float yaw;
    public float pitch;

    public FusionData(float kalAngleX, float kalAngleY) {
        this.yaw = kalAngleX;
        this.pitch = kalAngleY;
    }
}
