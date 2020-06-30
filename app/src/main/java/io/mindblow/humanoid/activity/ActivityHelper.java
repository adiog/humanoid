// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.activity;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

class ActivityHelper {
    static void onCreateButton(Activity activity, View.OnClickListener listener, int buttonId) {
        Button button = (Button) activity.findViewById(buttonId);
        button.setOnClickListener(listener);
    }
}
