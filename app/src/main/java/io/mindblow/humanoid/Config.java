// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid;

import android.util.DisplayMetrics;


public class Config {
    private int fps = 10;

    public int engineIntervalMillis = 50;
    public float engineIntervalSeconds = (float) engineIntervalMillis / 1000.0f;

    public int canvasIntervalMillis = 1000 / fps;
    public float canvasIntervalSeconds = (float) canvasIntervalMillis / 1000.0f;

    public DisplayMetrics displayMetrics = new DisplayMetrics();
}
