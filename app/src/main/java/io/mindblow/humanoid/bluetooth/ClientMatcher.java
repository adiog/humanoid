// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.bluetooth;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import io.mindblow.humanoid.canvas.model.Segment;

import static android.content.Context.MODE_PRIVATE;


public class ClientMatcher {
    private Map<String, Segment> matching = new HashMap<String, Segment>();

    public ClientMatcher(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("HUMANOID_PREFERENCES", MODE_PRIVATE);
        String mapping = preferences.getString("mapping", "");
        String[] mappingRows = mapping.split(";");
        for (String mappingEntry : mappingRows) {
            String[] mappingValues = mappingEntry.split("#");
            if (mappingValues.length == 2) {
                String address = mappingValues[0];
                Segment segment = Segment.values()[Integer.valueOf(mappingValues[1])];
                matching.put(address, segment);
            }
        }
    }

    public Segment match(String addressString) {
        return matching.get(addressString);
    }

}
