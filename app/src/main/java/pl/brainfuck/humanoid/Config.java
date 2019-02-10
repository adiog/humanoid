// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid;

import android.util.DisplayMetrics;


public class Config {
    private int fps = 10;

    public int engineIntervalMillis = 50;
    public float engineIntervalSeconds = (float) engineIntervalMillis / 1000.0f;

    public int canvasIntervalMillis = 1000 / fps;
    public float canvasIntervalSeconds = (float) canvasIntervalMillis / 1000.0f;

    DisplayMetrics displayMetrics = new DisplayMetrics();
}
